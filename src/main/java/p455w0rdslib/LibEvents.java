package p455w0rdslib;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.LibGlobals.ConfigOptions;
import p455w0rdslib.api.IChunkLoadable;
import p455w0rdslib.capabilities.CapabilityChunkLoader;
import p455w0rdslib.capabilities.CapabilityChunkLoader.ProviderTE;
import p455w0rdslib.util.ContributorUtils;

/**
 * @author p455w0rd
 *
 */
public class LibEvents {

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void tickStart(TickEvent.ClientTickEvent event) {
		if (event.phase != TickEvent.Phase.START || event.type != TickEvent.Type.CLIENT || event.side != Side.CLIENT) {
			return;
		}
		if (FMLClientHandler.instance().getWorldClient() != null) {
			LibGlobals.ELAPSED_TICKS++;
			//if (LibGlobals.TIME % 50 == 0) {
			LibGlobals.TIME2++;
			//}
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
	}

	@SideOnly(Side.CLIENT)
	@SubscribeEvent
	public void entityJoinWorld(EntityJoinWorldEvent event) {
		if (event.getEntity() instanceof AbstractClientPlayer) {
			AbstractClientPlayer player = (AbstractClientPlayer) event.getEntity();
			if (player.getName().equals(P455w0rdsLib.PROXY.getPlayer().getName()) && !ConfigOptions.ENABLE_CONTRIB_CAPE) {
				return;
			}
			try {
				ContributorUtils.queuePlayerCosmetics(player);
			}
			catch (Exception localException) {
			}
		}
	}

	@SubscribeEvent
	public void onPlace(PlaceEvent e) {
		World world = e.getWorld();
		BlockPos pos = e.getPos();
		if (world != null && pos != null && world.getTileEntity(pos) != null) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof IChunkLoadable) {
				if (tile.hasCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null)) {
					IChunkLoadable chunkLoader = (IChunkLoadable) tile;
					tile.getCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null).attachChunkLoader(chunkLoader.getModInstance());
				}
			}
		}
	}

	@SubscribeEvent
	public void blockBreak(BreakEvent e) {
		World world = e.getWorld();
		BlockPos pos = e.getPos();
		if (world != null && pos != null && world.getTileEntity(pos) != null) {
			TileEntity tile = world.getTileEntity(pos);
			if (tile instanceof IChunkLoadable) {
				if (tile.hasCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null)) {
					IChunkLoadable chunkLoader = (IChunkLoadable) tile;
					tile.getCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null).detachChunkLoader(chunkLoader.getModInstance());
				}
			}
		}
	}

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof IChunkLoadable) {
			TileEntity tile = event.getObject();
			IChunkLoadable chunkLoader = (IChunkLoadable) tile;
			if (chunkLoader.shouldChunkLoad()) {
				event.addCapability(new ResourceLocation(chunkLoader.getModID(), "chunkloader"), new ProviderTE(tile));
			}
		}
	}

}
