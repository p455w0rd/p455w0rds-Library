package p455w0rdslib.capabilities;

import java.util.*;

import org.apache.commons.lang3.tuple.Pair;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.Vec3i;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import p455w0rdslib.LibGlobals;
import p455w0rdslib.LibGlobals.Mods;
import p455w0rdslib.api.client.shader.Light;
import p455w0rdslib.util.CapabilityUtils.EmptyStorage;
import p455w0rdslib.util.RenderUtils;

/**
 * @author p455w0rd
 *
 */
public class CapabilityLightEmitter {

	public static final ResourceLocation CAP_LOCATION = new ResourceLocation(LibGlobals.MODID, "lights_cap");

	@CapabilityInject(ILightEmitter.class)
	public static Capability<ILightEmitter> LIGHT_EMITTER_CAPABILITY = null;

	private static final IStorage<ILightEmitter> STORAGE = new EmptyStorage<>();

	public static void register() {
		CapabilityManager.INSTANCE.register(ILightEmitter.class, STORAGE, DummyLightEmitter::new);
	}

	public static boolean checkCap(final Capability<?> capability) {
		return !Mods.ALBEDO.isLoaded() && capability == LIGHT_EMITTER_CAPABILITY;
	}

	public static boolean hasCap(final TileEntity tile) {
		return tile.hasCapability(LIGHT_EMITTER_CAPABILITY, null);
	}

	public static boolean hasCap(final Entity entity) {
		return entity.hasCapability(LIGHT_EMITTER_CAPABILITY, null);
	}

	public static boolean hasCap(final ItemStack stack) {
		return stack.hasCapability(LIGHT_EMITTER_CAPABILITY, null);
	}

	public static List<Light> getLights(final TileEntity tile) {
		return hasCap(tile) ? tile.getCapability(LIGHT_EMITTER_CAPABILITY, null).emitLight(new ArrayList<>(), tile) : Collections.emptyList();
	}

	public static List<Light> getLights(final Entity entity) {
		return hasCap(entity) ? entity.getCapability(LIGHT_EMITTER_CAPABILITY, null).emitLight(new ArrayList<>(), entity) : Collections.emptyList();
	}

	public static List<Light> getLights(final ItemStack stack, final Entity entity) {
		return hasCap(stack) ? stack.getCapability(LIGHT_EMITTER_CAPABILITY, null).emitLight(new ArrayList<>(), entity) : Collections.emptyList();
	}

	public static ICapabilityProvider getDummyProvider() {
		return DummyLightProvider.DUMMY_INSTANCE;
	}

	public static <T> T getDummyCapability() {
		return LIGHT_EMITTER_CAPABILITY.cast(new DummyLightEmitter());
	}

	private static class DummyLightEmitter implements ILightEmitter {

		@Override
		public List<Light> emitLight(final List<Light> lights, final Entity entity) {
			return Collections.emptyList();
		}

	}

	public static class DummyLightProvider implements ICapabilityProvider {

		ILightEmitter emitter;
		public static DummyLightProvider DUMMY_INSTANCE = new DummyLightProvider();

		private DummyLightProvider() {
			this(getDummyCapability());
		}

		public DummyLightProvider(final ILightEmitter emitter) {
			this.emitter = emitter;
		}

		@Override
		public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
			return checkCap(capability);
		}

		@Override
		public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
			return hasCapability(capability, null) ? LIGHT_EMITTER_CAPABILITY.cast(emitter) : null;
		}

	}

	public static class EntityLightEmitter extends DummyLightEmitter {

		Entity entity;

		public EntityLightEmitter(final Entity entity) {
			this.entity = entity;
		}

	}

	public static class StackLightEmitter extends DummyLightEmitter {

		ItemStack stack;

		public StackLightEmitter(final ItemStack stack) {
			this.stack = stack;
		}

	}

	public static class TileLightEmitter extends DummyLightEmitter {

		TileEntity tile;

		public TileLightEmitter(final TileEntity tile) {
			this.tile = tile;
		}

	}

	public static class EntityLightProvider extends DummyLightProvider {

		public EntityLightProvider(final Entity entity) {
			super(new EntityLightEmitter(entity));
		}

	}

	public static class StackLightProvider extends DummyLightProvider {

		public StackLightProvider(final ItemStack stack) {
			super(new StackLightEmitter(stack));
		}

	}

	public static class TileLightProvider extends DummyLightProvider {

		public TileLightProvider(final TileEntity tile) {
			super(new TileLightEmitter(tile));
		}

	}

	/*public static class StackProvider implements ICapabilityProvider {

		ILightEmitter emitter;

		public StackProvider(final ItemStack stack) {
			emitter = lights -> stack.getCapability(CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY, null).emitLight(lights);
		}

		@Override
		public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
			return capability == LIGHT_EMITTER_CAPABILITY;
		}

		@Override
		public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
			return hasCapability(capability, facing) ? LIGHT_EMITTER_CAPABILITY.cast(emitter) : null;
		}

	}*/

	public static <T> T getVanillaStackCapability(final ItemStack stack) {
		if (getColorForStack(stack).getLeft() != 0x0) {
			return CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY.cast(new StackLightEmitter(stack) {
				@Override
				public List<Light> emitLight(final List<Light> lights, final Entity entity) {
					final Vec3i c = RenderUtils.hexToRGB(getColorForStack(stack).getLeft());
					lights.add(Light.builder().pos(entity.posX, entity.posY, entity.posZ).color(c.getX(), c.getY(), c.getZ(), getColorForStack(stack).getRight().getRight()).radius(getColorForStack(stack).getRight().getLeft()).intensity(5.0f).build());
					return lights;
				}
			});
		}
		return null;
	}

	/*public static <T> T getVanillaTileCapability(final TileEntity tile) {
		if (getColorForTile(tile).getLeft() != 0x0) {
			return CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY.cast(new TileLightEmitter(tile) {
				@Override
				public List<Light> emitLight(final List<Light> lights, final Entity entity) {
					final Vec3i c = RenderUtils.hexToRGB(getColorForTile(tile).getLeft());
					lights.add(Light.builder().pos(entity.posX, entity.posY, entity.posZ).color(c.getX(), c.getY(), c.getZ(), getColorForTile(tile).getRight().getRight()).radius(getColorForTile(tile).getRight().getLeft()).intensity(5.0f).build());
					return lights;
				}
			});
		}
		return null;
	}*/

	public static ICapabilityProvider getVanillaStackProvider(final ItemStack stack) {

		return new ICapabilityProvider() {

			@Override
			public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {
				return checkCap(capability);
			}

			@Override
			public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
				return hasCapability(capability, null) ? getVanillaStackCapability(stack) : null;
			}

		};

	}

	//Pair.of(color, Pair.of(radius,brightness))
	private static final Map<Item, Pair<Integer, Pair<Float, Float>>> ITEM_COLOR_MAP = new HashMap<>();
	private static final Map<Block, Pair<Integer, Pair<Float, Float>>> BLOCK_COLOR_MAP = new HashMap<>();

	public static Pair<Integer, Pair<Float, Float>> getColorForBlock(final Block block) {
		if (block != null && BLOCK_COLOR_MAP.containsKey(block)) {
			return BLOCK_COLOR_MAP.get(block);
		}
		return Pair.of(0x0, Pair.of(0.0f, 0.0f));
	}

	public static Pair<Integer, Pair<Float, Float>> getColorForStack(final ItemStack stack) {
		if (BLOCK_COLOR_MAP.isEmpty()) {
			genColorMap();
		}
		int meta = stack.getMetadata();
		if (meta >= 16) {
			meta = 0;
		}
		final Block block = Block.getBlockFromItem(stack.getItem());
		if (block != null && BLOCK_COLOR_MAP.containsKey(block)) {
			return BLOCK_COLOR_MAP.get(block);
		}
		if (ITEM_COLOR_MAP.containsKey(stack.getItem())) {
			return ITEM_COLOR_MAP.get(stack.getItem());
		}
		return Pair.of(0x0, Pair.of(0.0f, 0.0f));
	}

	private static void genColorMap() {
		ITEM_COLOR_MAP.clear();
		BLOCK_COLOR_MAP.clear();
		BLOCK_COLOR_MAP.put(Blocks.REDSTONE_TORCH, Pair.of(0xFF770000, Pair.of(4.0f, 0.5f)));
		BLOCK_COLOR_MAP.put(Blocks.REDSTONE_WIRE, Pair.of(0xFF770000, Pair.of(1.0f, 1.0f)));
		BLOCK_COLOR_MAP.put(Blocks.TORCH, Pair.of(0x77885500, Pair.of(5.0f, 0.5f)));
		BLOCK_COLOR_MAP.put(Blocks.GLOWSTONE, Pair.of(0xFF777700, Pair.of(4.0f, 1.0f)));
		BLOCK_COLOR_MAP.put(Blocks.LIT_PUMPKIN, Pair.of(0xFF313100, Pair.of(12.0f, 0.25f)));
		BLOCK_COLOR_MAP.put(Blocks.REDSTONE_BLOCK, Pair.of(0x00770000, Pair.of(4.0f, 0.7f)));
		BLOCK_COLOR_MAP.put(Blocks.PORTAL, Pair.of(0xFF8F15D4, Pair.of(3.0f, 0.5f)));
		BLOCK_COLOR_MAP.put(Blocks.SEA_LANTERN, Pair.of(0x77889999, Pair.of(7.0f, 0.5f)));
		BLOCK_COLOR_MAP.put(Blocks.FIRE, Pair.of(0x77885500, Pair.of(5.0f, 0.5f)));
		ITEM_COLOR_MAP.put(Items.LAVA_BUCKET, Pair.of(0xFFFF5500, Pair.of(4.0f, 1.0f)));
		ITEM_COLOR_MAP.put(Items.REDSTONE, Pair.of(0xFF770000, Pair.of(4.0f, 0.25f)));
	}

	public interface ILightEmitter {

		default List<Light> emitLight(final List<Light> lights, final Entity entity) {
			return Collections.emptyList();
		}

		default List<Light> emitLight(final List<Light> lights, final TileEntity entity) {
			return Collections.emptyList();
		}

	}

}
