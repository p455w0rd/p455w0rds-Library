package p455w0rdslib.util;

import cofh.api.energy.IEnergyContainerItem;
import cofh.api.energy.IEnergyHandler;
import cofh.api.energy.IEnergyReceiver;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

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
		if (te instanceof IEnergyHandler) {
			IEnergyHandler handler = (IEnergyHandler) te;
			return handler;
		}
		return null;
	}

	public enum EnergyType {
			RF, Tesla, Forge, NONE
	}

}
