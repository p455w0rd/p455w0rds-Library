/*
 * The MIT License
 *
 * Copyright (c) 2017 Elucent, Una Thompson (unascribed), and contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package p455w0rdslib.api.client.shader;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import p455w0rdslib.LibGlobals.ConfigOptions;
import p455w0rdslib.LibShaders;
import p455w0rdslib.capabilities.CapabilityLightEmitter;
import p455w0rdslib.util.PlayerUtils;
import p455w0rdslib.util.RenderUtils;

@SuppressWarnings("deprecation")
public class LightHandler {

	public static final ArrayList<Light> lights = Lists.newArrayList();
	private static long frameId = 0;
	private static Vec3d camPos;
	private static Cam camera;
	public static Comparator<Light> distance = new Comparator<Light>() {

		@Override
		public int compare(final Light light0, final Light light1) {
			return Double.compare(getDistToLight(light0), getDistToLight(light1));
		}

		private double getDistToLight(final Light light) {
			return PlayerUtils.getDistanceToPos(light.x, light.y, light.z);
		}

	};

	public static void addLight(final Light l) {
		if (camPos.squareDistanceTo(l.x, l.y, l.z) > l.mag + ConfigOptions.SHADERS_MAX_DIST) {
			return;
		}
		if (camera != null && !camera.isBoundingBoxInFrustum(new AxisAlignedBB(l.x - l.mag, l.y - l.mag, l.z - l.mag, l.x + l.mag, l.y + l.mag, l.z + l.mag))) {
			return;
		}
		if (l != null) {
			lights.add(l);
		}
	}

	public static Light getLightForEntity(final Entity entity) {
		for (final Light light : lights) {
			if (light.entity != null && light.entity.getPosition().equals(entity.getPosition())) {
				return light;
			}
		}
		return null;
	}

	public static void uploadLights() {
		LibShaders.getActiveShader().getUniform("lightCount").setInt(lights.size());
		frameId++;
		if (frameId < ConfigOptions.SHADER_NUM_FRAMES_TO_SKIP + 1) {
			return;
		}
		frameId = 0;
		final int max = Math.min(lights.size(), ConfigOptions.MAX_LIGHTS);
		for (int i = 0; i < max; i++) {
			if (i < max) {
				final Light l = lights.get(i);
				LibShaders.getActiveShader().getUniform("lights[" + i + "].position").setFloat(l.x, l.y, l.z);
				LibShaders.getActiveShader().getUniform("lights[" + i + "].color").setFloat(l.r, l.g, l.b, l.a / 2);
				if (LibShaders.getActiveShader() == LibShaders.coloredLightShader) {
					LibShaders.getActiveShader().getUniform("lights[" + i + "].rad").setFloat(l.sx, l.sy, l.sz);
					LibShaders.getActiveShader().getUniform("lights[" + i + "].intensity").setFloat(l.l);
				}
			}
		}
	}

	public static void update(final World world) {
		if (!world.isRemote) {
			return;
		}
		final Minecraft mc = Minecraft.getMinecraft();
		final Entity cameraEntity = mc.getRenderViewEntity();
		if (cameraEntity != null) {
			final float pt = mc.getRenderPartialTicks();
			camPos = new Vec3d(cameraEntity.lastTickPosX + (cameraEntity.posX - cameraEntity.lastTickPosX) * pt, cameraEntity.lastTickPosY + (cameraEntity.posY - cameraEntity.lastTickPosY) * pt, cameraEntity.lastTickPosZ + (cameraEntity.posZ - cameraEntity.lastTickPosZ) * pt);
			camera = new Cam(camPos.x, camPos.y, camPos.z);
		}
		else {
			if (camPos == null) {
				camPos = new Vec3d(0, 0, 0);
			}
			camera = null;
			return;
		}
		final List<Light> toAdd = new ArrayList<>();

		for (final Entity e : world.getLoadedEntityList()) {
			if (e instanceof EntityItem) {
				if (CapabilityLightEmitter.hasCap(((EntityItem) e).getItem())) {
					toAdd.addAll(CapabilityLightEmitter.getLights(((EntityItem) e).getItem(), e));
				}
			}
			if (CapabilityLightEmitter.hasCap(e)) {
				toAdd.addAll(CapabilityLightEmitter.getLights(e));
				for (final ItemStack stack : e.getHeldEquipment()) {
					if (CapabilityLightEmitter.hasCap(stack)) {
						toAdd.addAll(CapabilityLightEmitter.getLights(stack, e));
					}
				}
				for (final ItemStack stack : e.getArmorInventoryList()) {
					if (CapabilityLightEmitter.hasCap(stack)) {
						toAdd.addAll(CapabilityLightEmitter.getLights(stack, e));
					}
				}
			}
		}
		for (final TileEntity t : world.loadedTileEntityList) {
			if (CapabilityLightEmitter.hasCap(t)) {
				toAdd.addAll(CapabilityLightEmitter.getLights(t));
			}
		}
		final List<BlockPos> blocksToRemove = new ArrayList<>();
		final ArrayList<BlockPos> blockCache = new ArrayList<>(getBlockListForWorld(world));

		for (final BlockPos pos : blockCache) {
			if (pos == null) {
				continue;
			}
			final IBlockState state = world.getBlockState(pos);
			final Block block = state.getBlock();
			if (state == null || world.isAirBlock(pos)) {
				blocksToRemove.add(pos.toImmutable());
			}
			else {
				if (block instanceof IBlockLightEmitter) {
					toAdd.addAll(((IBlockLightEmitter) block).emitLight(new ArrayList<>(), state, pos));
				}
				else {
					final Pair<Integer, Pair<Float, Float>> color = CapabilityLightEmitter.getColorForBlock(block);
					if (color.getLeft() != 0x0 && state != null) {
						final Vec3i c = RenderUtils.hexToRGB(color.getLeft());
						toAdd.add(Light.builder().pos(pos.up()).color(c.getX(), c.getY(), c.getZ(), color.getRight().getRight()).radius(color.getRight().getLeft()).intensity(5.0f).build());
					}
				}
			}
		}
		removeCachedPositions(world, blocksToRemove);
		toAdd.sort(distance);
		lights.clear();
		lights.addAll(ImmutableList.copyOf(toAdd));
	}

	private static final Map<String, ArrayList<BlockPos>> CACHED_BLOCKLIST = new HashMap<>();

	private static ArrayList<BlockPos> getBlockListForWorld(final World world) {
		final String dim = world.provider.getDimensionType().getName();
		if (isCached(dim)) {
			return CACHED_BLOCKLIST.get(dim);
		}
		final ArrayList<BlockPos> empty = new ArrayList<>();
		CACHED_BLOCKLIST.put(dim, empty);
		return empty;
	}

	private static boolean isCached(final String dimName) {
		final Map<String, ArrayList<BlockPos>> currentCache = new HashMap<>(CACHED_BLOCKLIST);
		for (final String cachedDim : currentCache.keySet()) {
			if (dimName.equals(cachedDim)) {
				return true;
			}
		}
		return false;
	}

	public static void addCachedPos(final World world, final BlockPos pos) {
		getBlockListForWorld(world).add(pos);
	}

	public static void removeCachedPositions(final World world, final List<BlockPos> positions) {
		final List<BlockPos> newList = new ArrayList<>();
		final ArrayList<BlockPos> worldBlocks = getBlockListForWorld(world);
		for (final BlockPos p : positions) {
			if (worldBlocks.contains(p)) {
				continue;
			}
			newList.add(p);
		}
		worldBlocks.clear();
		worldBlocks.addAll(newList);
	}

}