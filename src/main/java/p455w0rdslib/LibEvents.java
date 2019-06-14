package p455w0rdslib;

import org.lwjgl.input.Keyboard;

import com.google.common.collect.Lists;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.InitMapGenEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals.ConfigOptions;
import p455w0rdslib.LibGlobals.Mods;
import p455w0rdslib.api.IChunkLoadable;
import p455w0rdslib.api.client.ItemRenderingRegistry;
import p455w0rdslib.api.client.shader.IBlockLightEmitter;
import p455w0rdslib.api.client.shader.LightHandler;
import p455w0rdslib.api.event.BlockGenEvent;
import p455w0rdslib.capabilities.CapabilityChunkLoader;
import p455w0rdslib.capabilities.CapabilityChunkLoader.ProviderTE;
import p455w0rdslib.capabilities.CapabilityLightEmitter;
import p455w0rdslib.handlers.BrightnessHandler;
import p455w0rdslib.integration.Albedo;
import p455w0rdslib.util.ContributorUtils;

/**
 * @author p455w0rd
 *
 */
@SuppressWarnings("deprecation")
@EventBusSubscriber(modid = LibGlobals.MODID)
public class LibEvents {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void tickStart(final TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.START || event.type != TickEvent.Type.CLIENT || event.side != Side.CLIENT) {
			return;
		}
		if (LibGlobals.areShadersEnabled() && ConfigOptions.ENABLE_SHADERS) {
			BrightnessHandler.tickAllHandlers();
		}
		if (FMLClientHandler.instance().getWorldClient() != null) {
			if (Keyboard.isKeyDown(Keyboard.KEY_NUMPAD8)) {
				LibShaders.reload();
			}
			LibGlobals.ELAPSED_TICKS++;
			LibGlobals.TIME2++;
			if (LibGlobals.TIME2 > 360) {
				LibGlobals.TIME2 = 0;
			}
			if (LibGlobals.TURN == 0) {
				LibGlobals.GREEN += 15;
				if (LibGlobals.GREEN == 255) {
					LibGlobals.TURN = 1;
				}
			}
			if (LibGlobals.TURN == 1) {
				LibGlobals.RED -= 15;
				if (LibGlobals.RED == 0) {
					LibGlobals.TURN = 2;
				}
			}
			if (LibGlobals.TURN == 2) {
				LibGlobals.BLUE += 15;
				if (LibGlobals.BLUE == 255) {
					LibGlobals.TURN = 3;
				}
			}
			if (LibGlobals.TURN == 3) {
				LibGlobals.GREEN -= 15;
				if (LibGlobals.GREEN == 0) {
					LibGlobals.TURN = 4;
				}
			}
			if (LibGlobals.TURN == 4) {
				LibGlobals.RED += 15;
				if (LibGlobals.RED == 255) {
					LibGlobals.TURN = 5;
				}
			}
			if (LibGlobals.TURN == 5) {
				LibGlobals.BLUE -= 15;
				if (LibGlobals.BLUE == 0) {
					LibGlobals.TURN = 0;
				}
			}
		}
		else {
			if (LibGlobals.ELAPSED_TICKS != 0) {
				LibGlobals.ELAPSED_TICKS = 0;
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void onKeyBind(final KeyInputEvent event) {
		if (LibKeyBindings.TOGGLE_SHADERS.isPressed()) {
			if (LibGlobals.areShadersEnabled()) {
				ConfigOptions.ENABLE_SHADERS = !ConfigOptions.ENABLE_SHADERS;
				LibConfig.CONFIG.save();
			}
		}
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public static void entityJoinWorld(final EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof AbstractClientPlayer) {
			final AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
			if (player.getUniqueID().equals(P455w0rdsLib.PROXY.getPlayer().getUniqueID())) {
				if (!ConfigOptions.ENABLE_CONTRIB_CAPE) {
					return;
				}
			}
			try {
				ContributorUtils.queuePlayerCosmetics(player);
			}
			catch (final Exception localException) {
			}
		}
	}

	@SubscribeEvent
	public static void onPlace(final PlaceEvent e) {
		final World world = e.getWorld();
		final BlockPos pos = e.getPos();
		if (world != null && pos != null) {
			if (world.getTileEntity(pos) != null) {
				final TileEntity tile = world.getTileEntity(pos);
				if (tile instanceof IChunkLoadable) {
					if (tile.hasCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null)) {
						final IChunkLoadable chunkLoader = (IChunkLoadable) tile;
						tile.getCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null).attachChunkLoader(chunkLoader.getModInstance());
					}
				}
			}
			if (e.getPlacedBlock().getBlock() instanceof IBlockLightEmitter) {
				LightHandler.addCachedPos(world, pos);
			}
			else if (CapabilityLightEmitter.getColorForBlock(e.getState().getBlock()).getLeft() != 0x0) {
				LightHandler.addCachedPos(world, pos);
			}
		}
	}

	@SubscribeEvent
	public static void blockBreak(final BreakEvent e) {
		final World world = e.getWorld();
		final BlockPos pos = e.getPos();
		if (world != null && pos != null && world.getTileEntity(pos) != null) {
			final TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof IChunkLoadable) {
				if (tile.hasCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null)) {
					final IChunkLoadable chunkLoader = (IChunkLoadable) tile;
					tile.getCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null).detachChunkLoader(chunkLoader.getModInstance());
				}
			}
		}
		if (e.getState().getBlock() instanceof IBlockLightEmitter) {
			LightHandler.removeCachedPositions(e.getWorld(), Lists.newArrayList(e.getPos()));
		}
		else if (CapabilityLightEmitter.getColorForBlock(e.getState().getBlock()).getLeft() != 0x0) {
			LightHandler.removeCachedPositions(e.getWorld(), Lists.newArrayList(e.getPos()));
		}
	}

	@SubscribeEvent
	public static void attachTileCapabilities(final AttachCapabilitiesEvent<TileEntity> event) {
		final TileEntity tile = event.getObject();
		if (event.getObject() instanceof IChunkLoadable) {
			final IChunkLoadable chunkLoader = (IChunkLoadable) tile;
			if (chunkLoader.shouldChunkLoad()) {
				event.addCapability(new ResourceLocation(chunkLoader.getModID(), "chunkloader"), new ProviderTE(tile));
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void attachEntityCapabilities(final AttachCapabilitiesEvent<Entity> e) {
		if (Mods.ALBEDO.isLoaded()) {
			if (e.getObject() instanceof EntityPlayer) {
				e.addCapability(new ResourceLocation("pwlib:albedo_entity_cap"), Albedo.getEmptyProvider());
			}
		}
		else if (LibGlobals.areShadersEnabled() && ConfigOptions.ENABLE_SHADERS) {
			if (e.getObject() instanceof EntityPlayer) {
				e.addCapability(new ResourceLocation("pwlib:light_emitter_cap"), CapabilityLightEmitter.getDummyProvider());
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void attachStackCapabilities(final AttachCapabilitiesEvent<ItemStack> e) {
		final ItemStack stack = e.getObject();
		if (Mods.ALBEDO.isLoaded()) {
			if (CapabilityLightEmitter.getColorForStack(stack).getLeft() != 0x0) {
				e.addCapability(new ResourceLocation("pwlib:albedo_stack_cap"), Albedo.getVanillaStackProvider(stack));
			}
		}
		else if (LibGlobals.areShadersEnabled() && ConfigOptions.ENABLE_SHADERS) {
			if (CapabilityLightEmitter.getColorForStack(stack).getLeft() != 0x0) {
				if (!e.getObject().hasCapability(CapabilityLightEmitter.LIGHT_EMITTER_CAPABILITY, null)) {
					e.addCapability(new ResourceLocation("pwlib:light_emitter_cap"), CapabilityLightEmitter.getVanillaStackProvider(stack));
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public static void onMapGen(final InitMapGenEvent event) {
		switch (event.getType()) {
		case NETHER_BRIDGE:
		case END_CITY:
			return;
		default:
			//event.setNewGen(new BlockGenDetector(event.getOriginalGen()));
			break;
		}
	}

	@SubscribeEvent
	public static void onChunkLoad(final ChunkEvent.Load event) {
		final Chunk c = event.getChunk();
		final ChunkPos cPos = c.getPos();
		for (int x = cPos.getXStart(); x < cPos.getXEnd(); x++) {
			for (int z = cPos.getZStart(); z < cPos.getZEnd(); z++) {
				for (int y = 0; y <= 255; y++) {
					final IBlockState state = c.getBlockState(new BlockPos(x, y, z));
					if (state != null) {
						MinecraftForge.TERRAIN_GEN_BUS.post(new BlockGenEvent(event.getWorld(), new BlockPos(x, y, z), state));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public static void onBlockGen(final BlockGenEvent event) {
		final IBlockState state = event.getState();
		final World world = event.getWorld();
		final BlockPos pos = event.getPos();
		if (state != null) {
			final Block b = event.getState().getBlock();
			if (event.getPos() != null) {
				if (b instanceof IBlockLightEmitter) {
					LightHandler.addCachedPos(event.getWorld(), event.getPos());
				}
				else if (b != null) {
					if (b instanceof IBlockLightEmitter) {
						LightHandler.addCachedPos(world, pos);
					}
					else if (CapabilityLightEmitter.getColorForBlock(b).getLeft() != 0x0) {
						LightHandler.addCachedPos(world, pos);
					}
				}
			}
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelBake(final ModelBakeEvent event) {
		ItemRenderingRegistry.initModels(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(final ModelRegistryEvent event) {
		ItemRenderingRegistry.registerTEISRs(event);
	}

	@SubscribeEvent
	public static void onWorldUnload(final WorldEvent.Unload event) {

	}

}
