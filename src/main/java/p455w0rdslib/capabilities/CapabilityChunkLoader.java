package p455w0rdslib.capabilities;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.*;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import p455w0rdslib.util.CapabilityUtils.EmptyStorage;
import p455w0rdslib.util.ChunkUtils.TicketHandler;

/**
 * @author p455w0rd
 *
 */
public class CapabilityChunkLoader {

	@CapabilityInject(ICLTEHandler.class)
	public static Capability<ICLTEHandler> CAPABILITY_CHUNKLOADER_TE = null;

	@CapabilityInject(ICLEntityHandler.class)
	public static Capability<ICLEntityHandler> CAPABILITY_CHUNKLOADER_ENTITY = null;

	public static void register() {
		CapabilityManager.INSTANCE.register(ICLTEHandler.class, new EmptyStorage<ICLTEHandler>(), DefaultCLTEHandler::new);
		CapabilityManager.INSTANCE.register(ICLEntityHandler.class, new StorageEntity(), DefaultCLEntityHandler::new);
	}

	public static ICLTEHandler get(final TileEntity tile) {
		if (tile.hasCapability(CAPABILITY_CHUNKLOADER_TE, null)) {
			return tile.getCapability(CAPABILITY_CHUNKLOADER_TE, null);
		}
		return null;
	}

	public static ICLEntityHandler get(final Entity entity) {
		if (entity.hasCapability(CAPABILITY_CHUNKLOADER_ENTITY, null)) {
			return entity.getCapability(CAPABILITY_CHUNKLOADER_ENTITY, null);
		}
		return null;
	}

	/**
	 * Interface for dealing chunkloaders
	 *
	 * @author p455w0rd
	 *
	 */
	public interface ICLTEHandler {

		void setTileEntity(TileEntity te);

		void attachChunkLoader(Object modInstance);

		void detachChunkLoader(Object modInstance);

	}

	public interface ICLEntityHandler {

		/**
		 * @return An {@link java.util.ArrayList ArrayList} containing
		 *         {@link net.minecraft.util.math.BlockPos BlockPos} entries
		 *         that specify where this player's owned Chunk Loaders are
		 *         located
		 */
		ArrayList<BlockPos> getList();

		/**
		 * Adds a {@link net.minecraft.util.math.BlockPos BlockPos} entry
		 * specifying a Chunk Loader owned by the player
		 *
		 * @param pos
		 */
		void add(BlockPos pos);

		/**
		 * Removes a {@link net.minecraft.util.math.BlockPos BlockPos} entry
		 * specifying a Chunk Loader owned by the player
		 *
		 * @param pos
		 */
		void remove(BlockPos pos);

		/**
		 * sets a full list to a player-mainly used for persisting data after
		 * death
		 *
		 * @param list
		 */
		void set(ArrayList<BlockPos> list);

		/**
		 *
		 * @return total number of chunkloaders placed in world by the player
		 */
		int total();

	}

	public static class DefaultCLTEHandler implements ICLTEHandler {

		TileEntity tile;

		@Override
		public void setTileEntity(final TileEntity te) {
			tile = te;
		}

		@Override
		public void attachChunkLoader(final Object modInstance) {
			if (tile != null) {
				final TicketHandler handler = TicketHandler.getInstance();
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
					handler.load(tile.getWorld(), tile.getPos(), handler.getTicket(modInstance, tile.getWorld()));
				});
			}
		}

		@Override
		public void detachChunkLoader(final Object modInstance) {
			if (tile != null) {
				final TicketHandler handler = TicketHandler.getInstance();
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
					handler.unload(tile.getWorld(), tile.getPos(), handler.getTicket(modInstance, tile.getWorld()));
				});
			}
		}
	}

	public static class DefaultCLEntityHandler implements ICLEntityHandler {

		private ArrayList<BlockPos> chunkLoaderList = new ArrayList<>();

		@Override
		public ArrayList<BlockPos> getList() {
			return chunkLoaderList;
		}

		@Override
		public void add(final BlockPos pos) {
			chunkLoaderList.add(pos);
		}

		@Override
		public void remove(final BlockPos pos) {
			chunkLoaderList.remove(pos);
		}

		@Override
		public void set(final ArrayList<BlockPos> list) {
			chunkLoaderList = list;
		}

		@Override
		public int total() {
			return chunkLoaderList.size();
		}

	}

	/*public static class EmptyStorage extends CapabilityUtils.EmptyStorage<ICLTEHandler> {
	}*/

	public static class StorageEntity implements Capability.IStorage<ICLEntityHandler> {

		@Override
		public NBTBase writeNBT(final Capability<ICLEntityHandler> capability, final ICLEntityHandler instance, final EnumFacing side) {

			final NBTTagCompound properties = new NBTTagCompound();
			final NBTTagList ownedchunkloaders = new NBTTagList();
			properties.setTag("ownedchunkloaders", ownedchunkloaders);
			for (int i = 0; i < instance.getList().size(); i++) {
				final NBTTagCompound entry = new NBTTagCompound();
				entry.setLong("pos" + i, instance.getList().get(i).toLong());

				ownedchunkloaders.appendTag(entry);
			}
			return properties;

		}

		@Override
		public void readNBT(final Capability<ICLEntityHandler> capability, final ICLEntityHandler instance, final EnumFacing side, final NBTBase nbt) {

			final NBTTagCompound properties = (NBTTagCompound) nbt;
			if (properties.hasKey("ownedchunkloaders", Constants.NBT.TAG_LIST)) {
				final NBTTagList ownedchunkloaders = properties.getTagList("ownedchunkloaders", Constants.NBT.TAG_COMPOUND);
				final int count = ownedchunkloaders.tagCount();
				for (int i = 0; i < count; i++) {
					instance.add(BlockPos.fromLong(ownedchunkloaders.getCompoundTagAt(i).getLong("pos")));
				}
			}
		}
	}

	// Delegates all of the system calls to the capability.
	public static class ProviderTE implements ICapabilityProvider {

		ICLTEHandler instance;

		TileEntity tile;

		public ProviderTE(final TileEntity te) {
			tile = te;
			instance = new DefaultCLTEHandler();
			instance.setTileEntity(te);
		}

		@Override
		public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {

			return capability == CAPABILITY_CHUNKLOADER_TE;
		}

		@Override
		public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {
			return hasCapability(capability, facing) ? CAPABILITY_CHUNKLOADER_TE.cast(instance) : null;
		}

	}

	public static class ProviderEntity implements ICapabilitySerializable<NBTTagCompound> {

		ICLEntityHandler instance = CAPABILITY_CHUNKLOADER_ENTITY.getDefaultInstance();

		@Override
		public boolean hasCapability(final Capability<?> capability, final EnumFacing facing) {

			return capability == CAPABILITY_CHUNKLOADER_ENTITY;
		}

		@Override
		public <T> T getCapability(final Capability<T> capability, final EnumFacing facing) {

			return hasCapability(capability, facing) ? CAPABILITY_CHUNKLOADER_ENTITY.cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {

			return (NBTTagCompound) CAPABILITY_CHUNKLOADER_ENTITY.getStorage().writeNBT(CAPABILITY_CHUNKLOADER_ENTITY, instance, null);
		}

		@Override
		public void deserializeNBT(final NBTTagCompound nbt) {

			CAPABILITY_CHUNKLOADER_ENTITY.getStorage().readNBT(CAPABILITY_CHUNKLOADER_ENTITY, instance, null, nbt);
		}
	}

}
