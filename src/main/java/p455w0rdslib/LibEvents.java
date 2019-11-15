package p455w0rdslib;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals.ConfigOptions;
import p455w0rdslib.api.IChunkLoadable;
import p455w0rdslib.api.client.ItemRenderingRegistry;
import p455w0rdslib.capabilities.CapabilityChunkLoader;
import p455w0rdslib.capabilities.CapabilityChunkLoader.ProviderTE;
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
		if (FMLClientHandler.instance().getWorldClient() != null) {
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
	public static void onModelBake(final ModelBakeEvent event) {
		ItemRenderingRegistry.initModels(event);
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public static void onModelRegister(final ModelRegistryEvent event) {
		ItemRenderingRegistry.registerTEISRs(event);
	}

}
