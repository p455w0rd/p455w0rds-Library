package p455w0rdslib.util;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.CapabilityFluidHandler;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.IFluidTankProperties;
import net.minecraftforge.fluids.capability.wrappers.FluidHandlerWrapper;

/**
 * @author p455w0rd
 *
 */
@SuppressWarnings("deprecation")
public class FluidUtils {

	public static IFluidHandler getFluidHandler(TileEntity te, EnumFacing facing) {
		if (te.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing)) {
			return te.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, facing);
		}
		else if (te instanceof net.minecraftforge.fluids.IFluidHandler) {
			return getWrappedFluidHandler(te, facing);
		}
		return null;
	}

	public static IFluidHandler getWrappedFluidHandler(TileEntity te, EnumFacing facing) {
		if (te instanceof net.minecraftforge.fluids.IFluidHandler) {
			return new FluidHandlerWrapper((net.minecraftforge.fluids.IFluidHandler) te, facing);
		}
		return null;
	}

	public static int fill(TileEntity te, FluidStack fluid, int fluidVolume, EnumFacing facing) {
		return getFluidHandler(te, facing).fill(fluid, true);
	}

	public static int testFill(TileEntity te, FluidStack fluid, int fluidVolume, EnumFacing facing) {
		return getFluidHandler(te, facing).fill(fluid, false);
	}

	public static FluidStack drain(TileEntity te, int fluidVolume, EnumFacing facing) {
		return getFluidHandler(te, facing).drain(fluidVolume, true);
	}

	public static FluidStack testDrain(TileEntity te, int fluidVolume, EnumFacing facing) {
		return getFluidHandler(te, facing).drain(fluidVolume, false);
	}

	public static boolean hasRoomForFluid(TileEntity te, FluidStack fluid, EnumFacing facing) {
		IFluidHandler handler = getFluidHandler(te, facing);
		if (handler != null) {
			return hasRoomForFluid(handler, fluid);
		}
		return false;
	}

	public static boolean hasRoomForFluid(IFluidHandler handler, FluidStack fluid) {
		return getAvailableVolumeForFluid(handler, fluid) > 0;
	}

	public static boolean isFluidApplicable(IFluidHandler handler, FluidStack fluid) {
		for (IFluidTankProperties property : handler.getTankProperties()) {
			if (property.canFillFluidType(fluid)) {
				return true;
			}
		}
		return false;
	}

	public static int getTotalVolume(IFluidHandler handler) {
		int totalVolume = 0;
		for (IFluidTankProperties property : handler.getTankProperties()) {
			totalVolume += property.getCapacity();
		}
		return totalVolume;
	}

	public static int getAvailableVolume(IFluidHandler handler) {
		int availableVolume = 0;
		for (IFluidTankProperties property : handler.getTankProperties()) {
			availableVolume += property.getContents().amount;
		}
		return getTotalVolume(handler) - availableVolume;
	}

	public static int getTotalVolumeForFluid(IFluidHandler handler, FluidStack fluid) {
		int totalVolume = 0;
		for (IFluidTankProperties property : handler.getTankProperties()) {
			if (property.getCapacity() > 0 && property.getContents() == null) {
				totalVolume += property.getCapacity();
			}
			else {
				if (property.getContents().isFluidEqual(fluid)) {
					totalVolume += property.getCapacity();
				}
			}
		}
		return totalVolume;
	}

	public static int getAvailableVolumeForFluid(IFluidHandler handler, FluidStack fluid) {
		int availableVolume = 0;
		if (fluid != null && isFluidApplicable(handler, fluid)) {
			for (IFluidTankProperties property : handler.getTankProperties()) {
				if (property.getCapacity() > 0 && property.getContents() == null) {
					availableVolume += property.getCapacity();
				}
				else {
					if (property.getContents().isFluidEqual(fluid)) {
						availableVolume += getTotalVolumeForFluid(handler, fluid) - property.getContents().amount;
					}
				}
			}
		}
		return availableVolume;
	}

}
