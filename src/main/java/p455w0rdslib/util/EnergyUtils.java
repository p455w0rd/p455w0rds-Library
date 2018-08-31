package p455w0rdslib.util;

import javax.annotation.Nullable;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyProvider;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.energy.CapabilityEnergy;
import net.minecraftforge.energy.IEnergyStorage;

/**
 * @author p455w0rd
 *
 */
public class EnergyUtils {

	public static boolean isEnergyReceiverFromSide(TileEntity te, EnumFacing facing) {
		return (te instanceof IEnergyReceiver) ? ((IEnergyReceiver) te).canConnectEnergy(facing) : false;
	}

	public static ItemStack setFullPowerRF(ItemStack stack) {
		ItemStack fullItem = stack.copy();
		IEnergyContainerItem container = (IEnergyContainerItem) fullItem.getItem();
		int total = container.getMaxEnergyStored(fullItem);
		while (container.getEnergyStored(fullItem) < container.getMaxEnergyStored(fullItem)) {
			container.receiveEnergy(fullItem, total, false);
		}
		return fullItem;
	}

	public static int getRF(IEnergyHandler handler) {
		if (handler != null) {
			return handler.getEnergyStored(EnumFacing.DOWN);
		}
		return 0;
	}

	public static int getRFCapacity(IEnergyHandler handler) {
		if (handler != null) {
			return handler.getMaxEnergyStored(EnumFacing.DOWN);
		}
		return 0;
	}

	public static IEnergyHandler getRFTile(World world, BlockPos pos) {
		TileEntity te = world.getTileEntity(pos);
		if (te != null && te instanceof IEnergyHandler) {
			IEnergyHandler handler = (IEnergyHandler) te;
			return handler;
		}
		return null;
	}

	public static boolean isEnergyItem(ItemStack stack) {
		if (stack == null || stack.getItem() == null) {
			return false;
		}
		if (stack.getItem() instanceof IEnergyContainerItem) {
			return true;
		}
		return stack.hasCapability(CapabilityEnergy.ENERGY, null);
	}

	public static class EnergyWrapperRFtoFE implements IEnergyStorage {

		IEnergyHandler handler = null;
		IEnergyProvider provider = null;
		IEnergyReceiver receiver = null;
		IEnergyContainerItem container = null;
		ItemStack stack = null;
		EnumFacing side = null;

		public EnergyWrapperRFtoFE(TileEntity tile, @Nullable EnumFacing sideIn) {
			if (sideIn != null) {
				side = sideIn;
			}
			if (tile instanceof IEnergyProvider) {
				provider = (IEnergyProvider) tile;
			}
			if (tile instanceof IEnergyReceiver) {
				receiver = (IEnergyReceiver) tile;
			}
			if (tile instanceof IEnergyHandler) {
				handler = (IEnergyHandler) tile;
			}
		}

		public EnergyWrapperRFtoFE(ItemStack item) {
			stack = item;
			if (item.getItem() instanceof IEnergyContainerItem) {
				container = (IEnergyContainerItem) item.getItem();
			}
		}

		@Override
		public int receiveEnergy(int maxReceive, boolean simulate) {
			return receiver != null ? receiver.receiveEnergy(side, maxReceive, simulate) : container != null ? container.receiveEnergy(stack, maxReceive, simulate) : 0;
		}

		@Override
		public int extractEnergy(int maxExtract, boolean simulate) {
			return provider != null ? provider.extractEnergy(side, maxExtract, simulate) : container != null ? container.extractEnergy(stack, maxExtract, simulate) : 0;
		}

		@Override
		public int getEnergyStored() {
			return handler != null ? handler.getEnergyStored(side) : container != null ? container.getEnergyStored(stack) : 0;
		}

		@Override
		public int getMaxEnergyStored() {
			return handler != null ? handler.getMaxEnergyStored(side) : container != null ? container.getEnergyStored(stack) : 0;
		}

		@Override
		public boolean canExtract() {
			return getEnergyStored() > 0;
		}

		@Override
		public boolean canReceive() {
			return getEnergyStored() < getMaxEnergyStored();
		}

	}

	public enum EnergyType {
			RF, Tesla, Forge, NONE
	}

}
