package p455w0rdslib.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

/**
 * @author p455w0rd
 *
 */
public class ItemUtils {

	public static ItemStack getHelmet(final EntityPlayer player) {
		return player.inventory.getStackInSlot(39);
	}

	public static ItemStack getChestplate(final EntityPlayer player) {
		return player.inventory.getStackInSlot(38);
	}

	public static ItemStack getLeggings(final EntityPlayer player) {
		return player.inventory.getStackInSlot(37);
	}

	public static ItemStack getBoots(final EntityPlayer player) {
		return player.inventory.getStackInSlot(36);
	}

	public static boolean areItemTagsEqual(final ItemStack is1, final ItemStack itemStackIn) {
		final ItemStack newStack = is1.copy();
		if (newStack.hasTagCompound()) {
			if (newStack.getTagCompound().hasKey("p455w0rd.StackSize")) {
				newStack.getTagCompound().removeTag("p455w0rd.StackSize");
			}
			if (newStack.getTagCompound().hasNoTags()) {
				newStack.setTagCompound(null);
			}
		}
		return ItemStack.areItemStackTagsEqual(newStack, itemStackIn);
	}

	public static boolean areItemsEqual(final ItemStack is1, final ItemStack itemStackIn) {
		return is1.getItem() == itemStackIn.getItem() && is1.getItemDamage() == itemStackIn.getItemDamage() && areItemTagsEqual(is1, itemStackIn);
	}

	public static void dropItemStackInWorld(final World worldObj, final double x, final double y, final double z, final ItemStack stack) {
		final float f = 0.7F;
		final float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		final float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		final float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		final EntityItem entityitem = new EntityItem(worldObj, x + d0, y + d1, z + d2, stack);
		entityitem.setPickupDelay(10);
		if (stack.hasTagCompound()) {
			entityitem.getItem().setTagCompound(stack.getTagCompound().copy());
		}
		EasyMappings.spawn(worldObj, entityitem);
	}

	public static boolean readBoolean(final ItemStack is, final String key) {
		if (is == null) {
			return false;
		}
		final NBTTagCompound tag = getTag(is);
		return tag.hasKey(key) ? tag.getBoolean(key) : false;
	}

	public static int readInt(final ItemStack is, final String key) {
		if (is == null) {
			return 0;
		}
		final NBTTagCompound tag = getTag(is);
		return tag.hasKey(key) ? tag.getInteger(key) : -1;
	}

	public static void writeInt(final ItemStack is, final String key, final int value) {
		if (is == null) {
			return;
		}
		final NBTTagCompound tag = getTag(is);
		tag.setInteger(key, value);
	}

	public static void writeBoolean(final ItemStack is, final String key, final boolean value) {
		if (is == null) {
			return;
		}
		final NBTTagCompound tag = getTag(is);
		tag.setBoolean(key, value);
	}

	public static void delKey(final ItemStack is, final String key) {
		if (is == null) {
			return;
		}
		final NBTTagCompound tag = getTag(is);
		tag.removeTag(key);
	}

	public static NBTTagCompound getTag(final ItemStack is) {
		if (!is.hasTagCompound()) {
			is.setTagCompound(new NBTTagCompound());
		}
		return is.getTagCompound();
	}

	public static ItemStack readStack(final NBTTagCompound nbtTC, final String key) {
		return nbtTC.hasKey(key) ? new ItemStack(nbtTC) : null;
	}

	/*
	public static CapabilityDispatcher getCaps(ItemStack stack) {
		return stack.capabilities;//MCPrivateUtils.getItemStackCapabilities(stack);
	}
	
	public static void setItem(ItemStack stack, Item newItem) {
		Item item = stack.getItem();
		NBTTagCompound capNBT = MCPrivateUtils.getItemStackCapNBT(stack);
		if (newItem == item && stack != null && getCaps(stack) != null) //Item Didn't change but refreshed
		{
			net.minecraftforge.common.capabilities.ICapabilityProvider parent = item.initCapabilities(stack, getCaps(stack).serializeNBT());
			MCPrivateUtils.setItemStackCapabilities(stack, net.minecraftforge.event.ForgeEventFactory.gatherCapabilities(stack, parent));
		}
		else if (newItem != item && newItem != null) // Item Changed
		{
			net.minecraftforge.common.capabilities.ICapabilityProvider parent = newItem.initCapabilities(stack, capNBT);
			MCPrivateUtils.setItemStackCapabilities(stack, net.minecraftforge.event.ForgeEventFactory.gatherCapabilities(stack, parent));
		}
		if (capNBT != null && getCaps(stack) != null) {
			getCaps(stack).deserializeNBT(capNBT);
		}
		MCPrivateUtils.setItemStackDelegate(stack, newItem != null ? newItem.delegate : null);
		MCPrivateUtils.setItemStackItem(stack, newItem);
	}
	*/
	public static boolean areStacksSameSize(final ItemStack stackA, final ItemStack stackB) {
		return stackA == null && stackB == null || stackA != null && stackB != null && stackA.getCount() == stackB.getCount();
	}

}
