package p455w0rdslib.util;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.wrapper.InvWrapper;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
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

	public static boolean isItemHandler(TileEntity tile, EnumFacing facing) {
		return tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing) || tile instanceof IInventory || tile instanceof ISidedInventory;
	}

	public static boolean isInventory(TileEntity tile) {
		return tile instanceof IInventory || tile instanceof ISidedInventory;
	}

	public static IItemHandler getItemHandler(TileEntity tile, EnumFacing facing) {
		if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
			return tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		}
		if (tile instanceof ISidedInventory) {
			return new SidedInvWrapper(((ISidedInventory) tile), facing);
		}
		if (tile instanceof IInventory) {
			return new InvWrapper(((IInventory) tile));
		}
		return null;
	}

	public static ItemStack handleItemStack(IItemHandler handler, ItemStack stack, boolean simulate) {
		return ItemHandlerHelper.insertItem(handler, stack, simulate);
	}

	public static ItemStack insertStack(TileEntity tile, EnumFacing side, ItemStack stack) {
		return insertStack(tile, side, stack, false);
	}

	public static ItemStack insertStack(TileEntity tile, EnumFacing side, ItemStack stack, boolean simulate) {
		ItemStack newStack = null;
		if (side != null) {
			if (stack != null && isItemHandler(tile, side)) {
				if (getItemHandler(tile, side.getOpposite()) != null) {
					newStack = handleItemStack(getItemHandler(tile, side.getOpposite()), stack.copy(), simulate);
				}
			}
		}
		else {
			if (isItemHandler(tile, null)) {
				newStack = handleItemStack(getItemHandler(tile, null), stack.copy(), simulate);
			}
		}
		return newStack;
	}

	public static boolean invHasRoomForStack(IItemHandler inv, ItemStack stack) {
		if (stack == null) {
			return false;
		}
		for (int slot = 0; slot < inv.getSlots(); slot++) {
			ItemStack stackInSlot = inv.getStackInSlot(slot);
			int m;
			if (stackInSlot != null) {
				if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
					continue;
				}
				m = Math.min(stack.getMaxStackSize(), inv.getStackInSlot(slot).getMaxStackSize()) - stackInSlot.stackSize;
				if (stack.stackSize < m) {
					return true;
				}
			}
			else {
				return true;
			}
		}
		return false;
	}

	public static ItemStack insertItem(IInventory inv, ItemStack stack, boolean simulate) {
		if (stack == null || inv.getSizeInventory() <= 0) {
			return null;
		}
		for (int slot = 0; slot < inv.getSizeInventory(); slot++) {
			if (!inv.isItemValidForSlot(slot, stack)) {
				return stack;
			}
			ItemStack stackInSlot = inv.getStackInSlot(slot);

			int m;
			if (stackInSlot != null) {
				if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
					return stack;
				}

				m = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()) - stackInSlot.stackSize;

				if (stack.stackSize <= m) {
					if (!simulate) {
						ItemStack copy = stack.copy();
						copy.stackSize += stackInSlot.stackSize;
						inv.setInventorySlotContents(slot, copy);
						inv.markDirty();
					}

					return null;
				}
				else {
					// copy the stack to not modify the original one
					stack = stack.copy();
					if (!simulate) {
						ItemStack copy = stack.splitStack(m);
						copy.stackSize += stackInSlot.stackSize;
						inv.setInventorySlotContents(slot, copy);
						inv.markDirty();
						return stack;
					}
					else {
						stack.stackSize -= m;
						return stack;
					}
				}
			}
			else {
				m = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
				if (m < stack.stackSize) {
					// copy the stack to not modify the original one
					stack = stack.copy();
					if (!simulate) {
						inv.setInventorySlotContents(slot, stack.splitStack(m));
						inv.markDirty();
						return stack;
					}
					else {
						stack.stackSize -= m;
						return stack;
					}
				}
				else {
					if (!simulate) {
						inv.setInventorySlotContents(slot, stack);
						inv.markDirty();
					}
					return null;
				}
			}
		}
		return stack;
	}

}
