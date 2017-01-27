package p455w0rdslib.capabilities;

import java.util.ArrayList;

import net.minecraft.entity.Entity;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.FMLCommonHandler;
import p455w0rdslib.util.ChunkUtils.TicketHandler;

/**
 * @author p455w0rd
 *
 */
public class CapabilityChunkLoader {

	@CapabilityInject(ICLTEHandler.class)
	public static final Capability<ICLTEHandler> CAPABILITY_CHUNKLOADER_TE = null;

	@CapabilityInject(ICLEntityHandler.class)
	public static final Capability<ICLEntityHandler> CAPABILITY_CHUNKLOADER_ENTITY = null;

	public static void init() {
		CapabilityManager.INSTANCE.register(ICLTEHandler.class, new StorageTE(), DefaultCLTEHandler.class);
		CapabilityManager.INSTANCE.register(ICLEntityHandler.class, new StorageEntity(), DefaultCLEntityHandler.class);
	}

	public static ICLTEHandler get(TileEntity tile) {
		if (tile.hasCapability(CAPABILITY_CHUNKLOADER_TE, null)) {
			return tile.getCapability(CAPABILITY_CHUNKLOADER_TE, null);
		}
		return null;
	}

	public static ICLEntityHandler get(Entity entity) {
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
		public void setTileEntity(TileEntity te) {
			tile = te;
		}

		@Override
		public void attachChunkLoader(Object modInstance) {
			if (tile != null) {
				TicketHandler handler = TicketHandler.getInstance();
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
					handler.load(tile.getWorld(), tile.getPos(), handler.getTicket(modInstance, tile.getWorld()));
				});
			}
		}

		@Override
		public void detachChunkLoader(Object modInstance) {
			if (tile != null) {
				TicketHandler handler = TicketHandler.getInstance();
				FMLCommonHandler.instance().getMinecraftServerInstance().addScheduledTask(() -> {
					handler.unload(tile.getWorld(), tile.getPos(), handler.getTicket(modInstance, tile.getWorld()));
				});
			}
		}
	}

	public static class DefaultCLEntityHandler implements ICLEntityHandler {

		private ArrayList<BlockPos> chunkLoaderList = new ArrayList<BlockPos>();

		@Override
		public ArrayList<BlockPos> getList() {
			return chunkLoaderList;
		}

		@Override
		public void add(BlockPos pos) {
			chunkLoaderList.add(pos);
		}

		@Override
		public void remove(BlockPos pos) {
			chunkLoaderList.remove(pos);
		}

		@Override
		public void set(ArrayList<BlockPos> list) {
			chunkLoaderList = list;
		}

		@Override
		public int total() {
			return chunkLoaderList.size();
		}

	}

	public static class StorageTE implements Capability.IStorage<ICLTEHandler> {

		@Override
		public NBTBase writeNBT(Capability<ICLTEHandler> capability, ICLTEHandler instance, EnumFacing side) {

			return null;

		}

		@Override
		public void readNBT(Capability<ICLTEHandler> capability, ICLTEHandler instance, EnumFacing side, NBTBase nbt) {

		}
	}

	public static class StorageEntity implements Capability.IStorage<ICLEntityHandler> {

		@Override
		public NBTBase writeNBT(Capability<ICLEntityHandler> capability, ICLEntityHandler instance, EnumFacing side) {

			final NBTTagCompound properties = new NBTTagCompound();
			NBTTagList ownedchunkloaders = new NBTTagList();
			properties.setTag("ownedchunkloaders", ownedchunkloaders);
			for (int i = 0; i < instance.getList().size(); i++) {
				NBTTagCompound entry = new NBTTagCompound();
				entry.setLong("pos" + i, instance.getList().get(i).toLong());

				ownedchunkloaders.appendTag(entry);
			}
			return properties;

		}

		@Override
		public void readNBT(Capability<ICLEntityHandler> capability, ICLEntityHandler instance, EnumFacing side, NBTBase nbt) {

			final NBTTagCompound properties = (NBTTagCompound) nbt;
			if (properties.hasKey("ownedchunkloaders", Constants.NBT.TAG_LIST)) {
				NBTTagList ownedchunkloaders = properties.getTagList("ownedchunkloaders", Constants.NBT.TAG_COMPOUND);
				int count = ownedchunkloaders.tagCount();
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

		public ProviderTE(TileEntity te) {
			tile = te;
			instance = new DefaultCLTEHandler();
			instance.setTileEntity(te);
		}

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

			return capability == CAPABILITY_CHUNKLOADER_TE;
		}

		@SuppressWarnings("unchecked")
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			return hasCapability(capability, facing) ? (T) instance : null;
		}

	}

	public static class ProviderEntity implements ICapabilitySerializable<NBTTagCompound> {

		ICLEntityHandler instance = CAPABILITY_CHUNKLOADER_ENTITY.getDefaultInstance();

		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {

			return capability == CAPABILITY_CHUNKLOADER_ENTITY;
		}

		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {

			return hasCapability(capability, facing) ? CAPABILITY_CHUNKLOADER_ENTITY.<T>cast(instance) : null;
		}

		@Override
		public NBTTagCompound serializeNBT() {

			return (NBTTagCompound) CAPABILITY_CHUNKLOADER_ENTITY.getStorage().writeNBT(CAPABILITY_CHUNKLOADER_ENTITY, instance, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {

			CAPABILITY_CHUNKLOADER_ENTITY.getStorage().readNBT(CAPABILITY_CHUNKLOADER_ENTITY, instance, null, nbt);
		}
	}

}
