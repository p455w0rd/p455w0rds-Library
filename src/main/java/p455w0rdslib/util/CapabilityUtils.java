package p455w0rdslib.util;

import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;

/**
 * @author p455w0rd
 *
 */
public class CapabilityUtils {

	public static Capability<?> getCap(final ItemStack stack, final Capability<?> cap) {
		if (!stack.isEmpty()) {
			if (stack.hasCapability(cap, null)) {
				return (Capability<?>) stack.getCapability(cap, null);
			}
		}
		return null;
	}

	public static boolean isItemHandler(final Capability<?> capability) {
		return capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY;
	}

	public static <T> T getWrappedItemHandler(final IInventory inventory) {
		return CapabilityItemHandler.ITEM_HANDLER_CAPABILITY.cast(new InvWrapper(inventory));
	}

	public static class EmptyStorage<T> implements Capability.IStorage<T> {

		@Override
		public NBTBase writeNBT(final Capability<T> capability, final T instance, final EnumFacing side) {
			return null;
		}

		@Override
		public void readNBT(final Capability<T> capability, final T instance, final EnumFacing side, final NBTBase nbt) {
		}

	}

}
