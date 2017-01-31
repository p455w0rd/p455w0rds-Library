package p455w0rdslib.util;

import java.util.List;

import com.google.common.collect.Lists;

import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.items.CapabilityItemHandler;

/**
 * @author p455w0rd
 *
 */
public class TileEntityUtils {

	public static void markBlockForUpdate(World world, BlockPos pos) {
		if (world != null) {
			world.notifyBlockUpdate(pos, world.getBlockState(pos), world.getBlockState(pos), 3);
		}
	}

	public static TileEntity getAdjacentTile(World world, BlockPos pos, EnumFacing dir) {
		pos = pos.offset(dir);
		return world == null || !world.isBlockLoaded(pos) ? null : world.getTileEntity(pos);
	}

	public static TileEntity getAdjacentTile(World world, BlockPos pos, int side) {
		return world == null ? null : getAdjacentTile(world, pos, EnumFacing.VALUES[side]);
	}

	public static TileEntity getAdjacentTile(TileEntity te, EnumFacing dir) {
		return te == null ? null : getAdjacentTile(te.getWorld(), te.getPos(), dir);
	}

	public static TileEntity getAdjacentTile(TileEntity te, int side) {
		return te == null ? null : getAdjacentTile(te.getWorld(), te.getPos(), EnumFacing.VALUES[side]);
	}

	public static List<TileEntity> getAdjacentTiles(TileEntity te) {
		return getAdjacentTiles(te, false);
	}

	public static List<TileEntity> getAdjacentHorizontalTiles(TileEntity te) {
		return getAdjacentTiles(te, true);
	}

	private static List<TileEntity> getAdjacentTiles(TileEntity te, boolean horizontal) {
		List<TileEntity> tileList = Lists.newArrayList();
		for (EnumFacing facing : (horizontal ? EnumFacing.HORIZONTALS : EnumFacing.VALUES)) {
			tileList.add(getAdjacentTile(te, facing));
		}
		return tileList;
	}

	public static List<IEnergyHandler> getAdjacentRFTiles(TileEntity te) {
		return getAdjacentRFTiles(te, false);
	}

	public static List<IEnergyHandler> getAdjacentHorizontalRFTiles(TileEntity te) {
		return getAdjacentRFTiles(te, false);
	}

	private static List<IEnergyHandler> getAdjacentRFTiles(TileEntity te, boolean horizontal) {
		List<IEnergyHandler> tileList = Lists.newArrayList();
		for (TileEntity tile : (horizontal ? getAdjacentTiles(te, true) : getAdjacentTiles(te))) {
			if (tile != null && tile instanceof IEnergyHandler) {
				tileList.add((IEnergyHandler) tile);
			}
		}
		return tileList;
	}

	public static List<IEnergyReceiver> getAdjacentRFReceivers(TileEntity te, boolean horizontal) {
		List<IEnergyReceiver> tileList = Lists.newArrayList();
		for (IEnergyHandler tile : getAdjacentRFTiles(te, horizontal)) {
			if (tile instanceof IEnergyReceiver) {
				tileList.add((IEnergyReceiver) tile);
			}
		}
		return tileList;
	}

	public static List<IEnergyProvider> getAdjacentRFProviders(TileEntity te, boolean horizontal) {
		List<IEnergyProvider> tileList = Lists.newArrayList();
		for (IEnergyHandler tile : getAdjacentRFTiles(te, horizontal)) {
			if (tile instanceof IEnergyProvider) {
				tileList.add((IEnergyProvider) tile);
			}
		}
		return tileList;
	}

	public static List<TileEntity> getAdjacentInventoryTiles(TileEntity te) {
		return getAdjacentInventoryTiles(te, false);
	}

	public static List<TileEntity> getAdjacentHorizontalInventoryTiles(TileEntity te) {
		return getAdjacentInventoryTiles(te, true);
	}

	private static List<TileEntity> getAdjacentInventoryTiles(TileEntity te, boolean horizontal) {
		List<TileEntity> tileList = Lists.newArrayList();
		for (TileEntity tile : (horizontal ? getAdjacentTiles(te, true) : getAdjacentTiles(te))) {
			if (tile != null) {
				if (InventoryUtils.isItemHandler(tile, null) && InventoryUtils.getItemHandler(tile, null) != null) {
					if (!tileList.contains(tile)) {
						tileList.add(tile);
					}
				}
				for (int i = 0; i < 6; i++) {
					if (InventoryUtils.isItemHandler(tile, EnumFacing.VALUES[i]) && InventoryUtils.getItemHandler(tile, EnumFacing.VALUES[i]) != null) {
						if (!tileList.contains(tile)) {
							tileList.add(tile);
						}
					}
				}
			}
		}
		return tileList;
	}

	public static List<TileEntity> getAdjacentFluidTiles(TileEntity te) {
		return getAdjacentFluidTiles(te, false);
	}

	public static List<TileEntity> getAdjacentHorizontalFluidTiles(TileEntity te) {
		return getAdjacentFluidTiles(te, true);
	}

	private static List<TileEntity> getAdjacentFluidTiles(TileEntity te, boolean horizontal) {
		List<TileEntity> tileList = Lists.newArrayList();
		for (EnumFacing facing : (horizontal ? EnumFacing.HORIZONTALS : EnumFacing.VALUES)) {
			if (te == null || te.getWorld() == null || te.getPos() == null) {
				break;
			}
			BlockPos pos = te.getPos();
			TileEntity currentTile = te.getWorld().getTileEntity(pos.offset(facing));
			if (currentTile != null) {
				if (currentTile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing.getOpposite()) || currentTile.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null)) {
					tileList.add(currentTile);
				}
			}
		}
		return tileList;
	}

	public static List<TileEntity> getAdjacentItemTiles(TileEntity te) {
		return getAdjacentItemTiles(te, false);
	}

	public static List<TileEntity> getAdjacentHorizontalItemTiles(TileEntity te) {
		return getAdjacentItemTiles(te, true);
	}

	private static List<TileEntity> getAdjacentItemTiles(TileEntity te, boolean horizontal) {
		List<TileEntity> tileList = Lists.newArrayList();
		for (EnumFacing facing : (horizontal ? EnumFacing.HORIZONTALS : EnumFacing.VALUES)) {
			if (te == null || te.getWorld() == null || te.getPos() == null) {
				break;
			}
			BlockPos pos = te.getPos();
			TileEntity currentTile = te.getWorld().getTileEntity(pos.offset(facing));
			if (currentTile != null) {
				if (currentTile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing.getOpposite()) || currentTile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, null)) {
					tileList.add(currentTile);
				}
			}
		}
		return tileList;
	}

	public static List<TileEntity> getAdjacentFETiles(TileEntity te) {
		return getAdjacentFETiles(te, false);
	}

	public static List<TileEntity> getAdjacentHorizontalFETiles(TileEntity te) {
		return getAdjacentFETiles(te, true);
	}

	private static List<TileEntity> getAdjacentFETiles(TileEntity te, boolean horizontal) {
		List<TileEntity> tileList = Lists.newArrayList();
		for (EnumFacing facing : (horizontal ? EnumFacing.HORIZONTALS : EnumFacing.VALUES)) {
			if (te == null || te.getWorld() == null || te.getPos() == null) {
				break;
			}
			BlockPos pos = te.getPos();
			TileEntity currentTile = te.getWorld().getTileEntity(pos.offset(facing));
			if (currentTile != null) {
				if (currentTile.hasCapability(CapabilityEnergy.ENERGY, facing.getOpposite()) || currentTile.hasCapability(CapabilityEnergy.ENERGY, null)) {
					tileList.add(currentTile);
				}
			}
		}
		return tileList;
	}
	// TODO add Tesla..maybe
}
