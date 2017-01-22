package p455w0rdslib.util;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import p455w0rdslib.P455w0rdsLib;

/**
 * @author p455w0rd
 *
 */
public class InventoryUtils {

	@SideOnly(Side.CLIENT)
	public static ItemStack getArmorPiece(EntityEquipmentSlot slot) {
		if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
			EntityPlayer player = P455w0rdsLib.PROXY.getPlayer();
			ItemStack[] armorStack = player.inventory.armorInventory;
			int slotNum = 0;
			switch (slot) {
			case HEAD:
				slotNum = 3;
				break;
			case CHEST:
				slotNum = 2;
				break;
			case LEGS:
				slotNum = 1;
				break;
			default:
			case FEET:
				slotNum = 0;
				break;
			}
			return armorStack[slotNum];
		}
		return null;
	}

	public static boolean isInHotbar(int index) {
		return (index >= 0) && (index <= 8);
	}

	public static boolean isInInventory(int index) {
		return (index >= 9) && (index <= 36);
	}

	public static boolean moveStackWithinInventory(ItemStack itemStackIn, int index, List<Slot> inventorySlots) {
		if (isInHotbar(index)) {
			for (int i = 9; i <= 36; i++) {
				Slot possiblyOpenSlot = inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					inventorySlots.get(index).putStack(null);
					return true;
				}
			}
		}
		else if (isInInventory(index)) {
			for (int i = 0; i <= 8; i++) {
				Slot possiblyOpenSlot = inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					inventorySlots.get(index).putStack(null);
					return true;
				}
			}
		}
		return false;
	}

	public static boolean moveStackToInventory(ItemStack itemStackIn, List<Slot> inventorySlots) {
		for (int i = 0; i <= 36; i++) {
			Slot possiblyOpenSlot = inventorySlots.get(i);
			if (!possiblyOpenSlot.getHasStack()) {
				possiblyOpenSlot.putStack(itemStackIn);
				return true;
			}
		}
		return false;
	}

	public static int getNextAvailableSlot(List<Slot> inventorySlots) {
		for (int i = 36; i <= 63; i++) {
			Slot s = inventorySlots.get(i);
			if ((s != null) && (s.getStack() == null)) {
				return i;
			}
		}
		return -1;
	}

	public static boolean isCustomSlot(int index, boolean withArmor, boolean withOffHand) {
		return index >= (withArmor ? 40 : withOffHand ? 41 : 36);
	}

	public static boolean isCustomSlot(int index, boolean withArmor) {
		return isCustomSlot(index, withArmor, false);
	}

	public static boolean isCustomSlot(int index) {
		return isCustomSlot(index, false, false);
	}

}
