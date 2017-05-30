package p455w0rdslib.util;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;

/**
 * @author p455w0rd
 *
 */
public class ItemUtils {

	public static ItemStack getHelmet(EntityPlayer player) {
		return player.inventory.getStackInSlot(39);
	}

	public static ItemStack getChestplate(EntityPlayer player) {
		return player.inventory.getStackInSlot(38);
	}

	public static ItemStack getLeggings(EntityPlayer player) {
		return player.inventory.getStackInSlot(37);
	}

	public static ItemStack getBoots(EntityPlayer player) {
		return player.inventory.getStackInSlot(36);
	}

	public static boolean areItemTagsEqual(ItemStack is1, ItemStack itemStackIn) {
		ItemStack newStack = is1.copy();
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

	public static boolean areItemsEqual(ItemStack is1, ItemStack itemStackIn) {
		return (is1.getItem() == itemStackIn.getItem()) && (is1.getItemDamage() == itemStackIn.getItemDamage()) && (areItemTagsEqual(is1, itemStackIn));
	}

	public static void dropItemStackInWorld(World worldObj, double x, double y, double z, ItemStack stack) {
		float f = 0.7F;
		float d0 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d1 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		float d2 = worldObj.rand.nextFloat() * f + (1.0F - f) * 0.5F;
		EntityItem entityitem = new EntityItem(worldObj, x + d0, y + d1, z + d2, stack);
		entityitem.setPickupDelay(10);
		if (stack.hasTagCompound()) {
			entityitem.getEntityItem().setTagCompound(stack.getTagCompound().copy());
		}
		EasyMappings.spawn(worldObj, entityitem);
	}

	public static boolean readBoolean(ItemStack is, String key) {
		if (is == null) {
			return false;
		}
		NBTTagCompound tag = getTag(is);
		return (tag.hasKey(key) ? tag.getBoolean(key) : false);
	}

	public static int readInt(ItemStack is, String key) {
		if (is == null) {
			return 0;
		}
		NBTTagCompound tag = getTag(is);
		return (tag.hasKey(key) ? tag.getInteger(key) : -1);
	}

	public static void writeInt(ItemStack is, String key, int value) {
		if (is == null) {
			return;
		}
		NBTTagCompound tag = getTag(is);
		tag.setInteger(key, value);
	}

	public static void writeBoolean(ItemStack is, String key, boolean value) {
		if (is == null) {
			return;
		}
		NBTTagCompound tag = getTag(is);
		tag.setBoolean(key, value);
	}

	public static void delKey(ItemStack is, String key) {
		if (is == null) {
			return;
		}
		NBTTagCompound tag = getTag(is);
		tag.removeTag(key);
	}

	public static NBTTagCompound getTag(ItemStack is) {
		if (!is.hasTagCompound()) {
			is.setTagCompound(new NBTTagCompound());
		}
		return is.getTagCompound();
	}

	public static ItemStack readStack(NBTTagCompound nbtTC, String key) {
		return (nbtTC.hasKey(key) ? new ItemStack(nbtTC) : null);
	}

	public static CapabilityDispatcher getCaps(ItemStack stack) {
		return MCPrivateUtils.getItemStackCapabilities(stack);
	}

	public static void setItem(ItemStack stack, Item newItem) {
		Item item = stack.getItem();
		NBTTagCompound capNBT = MCPrivateUtils.getItemStackCapNBT(stack);
		if (newItem == item && stack != null && getCaps(stack) != null) //Item Didn't change but refreshed
		{
			net.minecraftforge.common.capabilities.ICapabilityProvider parent = item.initCapabilities(stack, getCaps(stack).serializeNBT());
			MCPrivateUtils.setItemStackCapabilities(stack, net.minecraftforge.event.ForgeEventFactory.gatherCapabilities(item, stack, parent));
		}
		else if (newItem != item && newItem != null) // Item Changed
		{
			net.minecraftforge.common.capabilities.ICapabilityProvider parent = newItem.initCapabilities(stack, capNBT);
			MCPrivateUtils.setItemStackCapabilities(stack, net.minecraftforge.event.ForgeEventFactory.gatherCapabilities(newItem, stack, parent));
		}
		if (capNBT != null && getCaps(stack) != null) {
			getCaps(stack).deserializeNBT(capNBT);
		}
		MCPrivateUtils.setItemStackDelegate(stack, newItem != null ? newItem.delegate : null);
		MCPrivateUtils.setItemStackItem(stack, newItem);
	}

	public static boolean areStacksSameSize(ItemStack stackA, ItemStack stackB) {
		return (stackA == null && stackB == null) || (stackA != null && stackB != null && stackA.getCount() == stackB.getCount());
	}

}
