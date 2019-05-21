package p455w0rdslib.util;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;

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
