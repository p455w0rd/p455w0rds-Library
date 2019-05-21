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

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import p455w0rdslib.LibGlobals.ConfigOptions;
import p455w0rdslib.LibShaders;

public class LightHandler {

	public static final ArrayList<Light> lights = Lists.newArrayList();
	private static long frameId = 0;
	public static LightComparator distance = new LightComparator();
	private static Vec3d camPos;
	private static Cam camera;

	public static void addLight(final Light l) {
		if (camPos.squareDistanceTo(l.x, l.y, l.z) > l.mag + 128) {
			return;
		}
		if (camera != null && !camera.isBoundingBoxInFrustum(new AxisAlignedBB(l.x - l.mag, l.y - l.mag, l.z - l.mag, l.x + l.mag, l.y + l.mag, l.z + l.mag))) {
			return;
		}
		if (l != null) {
			lights.add(l);
		}
	}

	public static void uploadLights() {
		LibShaders.getActiveShader().getUniform("lightCount").setInt(lights.size());
		frameId++;
		if (frameId < ConfigOptions.SHADER_NUM_FRAMES_TO_SKIP + 1) {
			return;
		}
		frameId = 0;
		for (int i = 0; i < lights.size(); i++) {
			if (i < lights.size()) {
				final Light l = lights.get(i);
				LibShaders.getActiveShader().getUniform("lights[" + i + "].position").setFloat(l.x, l.y, l.z);
				LibShaders.getActiveShader().getUniform("lights[" + i + "].color").setFloat(l.r, l.g, l.b, l.a);
				LibShaders.getActiveShader().getUniform("lights[" + i + "].rad").setFloat(l.sx, l.sy, l.sz);
				LibShaders.getActiveShader().getUniform("lights[" + i + "].intensity").setFloat(l.l);
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
			if (e instanceof EntityPlayer) {
				final EntityPlayer p = (EntityPlayer) e;
				for (final ItemStack stack : p.getHeldEquipment()) {
					if (stack.getItem() instanceof IColoredLightEmitter) {
						final List<Light> tmpList = new ArrayList<>();
						((IColoredLightEmitter) stack.getItem()).emitLight(tmpList, p);
						toAdd.addAll(tmpList);
					}
					else if (stack.getItem() instanceof ItemBlock) {
						final Block block = ((ItemBlock) stack.getItem()).getBlock();
						if (block.hasTileEntity(block.getDefaultState())) {
							final TileEntity tile = block.createTileEntity(Minecraft.getMinecraft().world, block.getDefaultState());
							if (tile instanceof IColoredLightEmitter) {
								tile.setWorld(Minecraft.getMinecraft().world);
								tile.setPos(p.getPosition());
								final List<Light> tmpList = new ArrayList<>();
								((IColoredLightEmitter) tile).emitLight(tmpList, p);
								toAdd.addAll(tmpList);
							}
						}
					}
				}
			}
			if (e instanceof EntityItem) {
				final EntityItem ei = (EntityItem) e;
				if (ei.getItem().getItem() instanceof IColoredLightEmitter) {
					final List<Light> tmpList = new ArrayList<>();
					((IColoredLightEmitter) ei.getItem().getItem()).emitLight(tmpList, ei);
					toAdd.addAll(tmpList);
				}
			}
		}
		for (final TileEntity t : world.loadedTileEntityList) {
			if (t instanceof IColoredLightEmitter) {
				final List<Light> tmpList = new ArrayList<>();
				((IColoredLightEmitter) t).emitLight(tmpList, t);
				toAdd.addAll(tmpList);
			}
		}
		toAdd.sort(distance);
		clear();
		lights.addAll(ImmutableList.copyOf(toAdd));
	}

	/*private static List<Light> getLights(final TileEntity tile) {
		final List<Light> tmpList = new ArrayList<>();
		if (tile != null) {
			if (tile.hasCapability(CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY, null)) {
				tile.getCapability(CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY, null).emitLight(tmpList);
			}
			else {
				for (final EnumFacing side : EnumFacing.VALUES) {
					if (tile.hasCapability(CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY, side)) {
						tile.getCapability(CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY, side).emitLight(tmpList);
					}
				}
			}
		}
		return tmpList;
	}*/

	public static void clear() {
		lights.clear();
	}

	private static class LightComparator implements Comparator<Light> {

		@Override
		public int compare(final Light light0, final Light light1) {
			final EntityPlayer p = Minecraft.getMinecraft().player;
			return Double.compare(getDistanceSq(light0.x, light0.y, light0.z, p.posX, p.posY, p.posZ), getDistanceSq(light1.x, light1.y, light1.z, p.posX, p.posY, p.posZ));
		}

		private static double getDistanceSq(final double x1, final double y1, final double z1, final double x2, final double y2, final double z2) {
			return Math.pow(x1 - x2, 2.0) + Math.pow(y1 - y2, 2.0) + Math.pow(z1 - z2, 2.0);
		}

	}
}