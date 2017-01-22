package p455w0rdslib;

import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.BlockEvent.PlaceEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
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
					tile.getCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null).attachChunkLoader(P455w0rdsLib.INSTANCE);
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
					tile.getCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null).detachChunkLoader(P455w0rdsLib.INSTANCE);
				}
			}
		}
	}

	@SubscribeEvent
	public void attachCapabilities(AttachCapabilitiesEvent<TileEntity> event) {
		if (event.getObject() instanceof IChunkLoadable) {
			TileEntity tile = event.getObject();
			event.addCapability(new ResourceLocation(LibGlobals.MODID, "chunkloader"), new ProviderTE(tile));
		}
	}

	private void loadChunk(World world, int chunkX, int chunkZ) {
		int minX = chunkX << 4;
		int minZ = chunkZ << 4;
		int maxX = minX + 16;
		int maxZ = minZ + 16;
		for (int y = 0; y <= 256; y++) {
			for (int x = minX; x < maxX; x++) {
				for (int z = minZ; z < maxZ; z++) {
					BlockPos pos = new BlockPos(x, y, z);
					if (world.getTileEntity(pos) != null) {
						TileEntity te = world.getTileEntity(pos);
						if (te.hasCapability(CapabilityChunkLoader.CAPABILITY_CHUNKLOADER_TE, null)) {
							//CapabilityChunkLoader.get(te).attachChunkLoader();
						}
					}
				}
			}
		}
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChunkLoadPopulate(PopulateChunkEvent.Post event) {
		LibGlobals.THREAD_POOL.submit(() -> loadChunk(event.getWorld(), event.getChunkX(), event.getChunkZ()));
	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onChunkLoad(ChunkEvent.Load event) {
		LibGlobals.THREAD_POOL.submit(() -> loadChunk(event.getWorld(), event.getChunk().xPosition, event.getChunk().zPosition));
	}
}
