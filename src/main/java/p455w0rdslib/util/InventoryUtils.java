package p455w0rdslib.util;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
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

	public static boolean isItemHandler(TileEntity tile) {
		return isItemHandler(tile, null);
	}

	public static boolean isItemHandler(TileEntity tile, @Nullable EnumFacing facing) {
		return tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
	}

	public static boolean isInventory(TileEntity tile) {
		return tile instanceof IInventory;
	}

	public static boolean hasItemStorage(TileEntity tile, EnumFacing facing) {
		return isItemHandler(tile, facing) || isInventory(tile);
	}

	public static IItemHandler getItemHandler(TileEntity tile, EnumFacing facing) {
		if (tile.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
			return tile.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
		}
		if (tile instanceof ISidedInventory) {
			return getWrappedInventory(((ISidedInventory) tile), facing);
		}
		if (tile instanceof IInventory) {
			return getWrappedInventory(((IInventory) tile));
		}
		return null;
	}

	public static IItemHandler getWrappedInventory(IInventory inventory) {
		return getWrappedInventory(inventory, null);
	}

	public static IItemHandler getWrappedInventory(IInventory inventory, @Nullable EnumFacing facing) {
		if (inventory instanceof ISidedInventory) {
			return new SidedInvWrapper(((ISidedInventory) inventory), facing);
		}
		return new InvWrapper(inventory);
	}

	public static boolean addItemStack(ItemStack[] inventory, ItemStack stack, int startIndex, int endIndex) {
		if (stack == null) {
			return true;
		}
		int openSlot = -1;
		for (int i = startIndex; i <= endIndex; i++) {
			if (ItemStack.areItemStacksEqual(stack, inventory[i]) && inventory[i].getMaxStackSize() > inventory[i].stackSize) {
				int hold = inventory[i].getMaxStackSize() - inventory[i].stackSize;
				if (hold >= stack.stackSize) {
					inventory[i].stackSize += stack.stackSize;
					stack = null;
					return true;
				}
				else {
					stack.stackSize -= hold;
					inventory[i].stackSize += hold;
				}
			}
			else if (inventory[i] == null && openSlot == -1) {
				openSlot = i;
			}
		}
		if (openSlot >= 0) {
			inventory[openSlot] = stack;
		}
		else {
			return false;
		}
		return true;
	}

	public static ItemStack insertItem(IInventory inventory, ItemStack stack) {
		return insertItem(inventory, stack, null);
	}

	public static ItemStack insertItem(IInventory inventory, ItemStack stack, @Nullable EnumFacing side) {
		if (stack == null || inventory == null) {
			return null;
		}
		int stackSize = stack.stackSize;
		if (inventory instanceof ISidedInventory) {
			ISidedInventory sidedInv = (ISidedInventory) inventory;
			int slots[] = sidedInv.getSlotsForFace(side);

			if (slots == null) {
				return stack;
			}
			for (int i = 0; i < slots.length && stack != null; i++) {
				if (sidedInv.canInsertItem(slots[i], stack, side)) {
					ItemStack existingStack = inventory.getStackInSlot(slots[i]);
					if (ItemStack.areItemStacksEqual(stack, existingStack)) {
						stack = insertMergeable(sidedInv, slots[i], stack, existingStack);
					}
				}
			}
			for (int i = 0; i < slots.length && stack != null; i++) {
				if (inventory.getStackInSlot(slots[i]) == null && sidedInv.canInsertItem(slots[i], stack, side)) {
					stack = insertEmpty(sidedInv, slots[i], stack);
				}
			}
		}
		else {
			int invSize = inventory.getSizeInventory();
			for (int i = 0; i < invSize && stack != null; i++) {
				ItemStack existingStack = inventory.getStackInSlot(i);
				if (ItemStack.areItemStacksEqual(stack, existingStack)) {
					stack = insertMergeable(inventory, i, stack, existingStack);
				}
			}
			for (int i = 0; i < invSize && stack != null; i++) {
				if (inventory.getStackInSlot(i) == null) {
					stack = insertEmpty(inventory, i, stack);
				}
			}
		}
		if (stack == null || stack.stackSize != stackSize) {
			inventory.markDirty();
		}
		return stack;
	}

	public static boolean hasRoomForStack(TileEntity te, ItemStack stack) {
		if (isItemHandler(te)) {
			if (hasRoomForStack(getItemHandler(te, null), stack)) {
				return true;
			}
			for (int i = 0; i < EnumFacing.values().length; i++) {
				if (hasRoomForStack(getItemHandler(te, EnumFacing.values()[i]), stack)) {
					return true;
				}
			}
		}
		if (isInventory(te)) {
			return hasRoomForStack((IInventory) te, stack);
		}
		return false;
	}

	public static boolean hasRoomForStack(IInventory inventory, ItemStack stack) {
		return hasRoomForStack(inventory, stack, null);
	}

	public static boolean hasRoomForStack(IInventory inventory, ItemStack stack, @Nullable EnumFacing facing) {
		IInventory tempInventory = getInventoryCopy(inventory);
		if (facing == null) {
			return ItemStack.areItemStacksEqual(stack, insertItem(tempInventory, stack.copy()));
		}
		for (int i = 0; i < EnumFacing.values().length; i++) {
			if (ItemStack.areItemStacksEqual(stack, insertItem(tempInventory, stack.copy(), EnumFacing.values()[i]))) {
				return true;
			}
		}
		return false;
	}

	public static boolean hasRoomForStack(IItemHandler handler, ItemStack stack) {
		IItemHandler tempHandler = null;
		if (handler instanceof IItemHandlerModifiable) {
			tempHandler = getItemHandlerModifiableCopy((IItemHandlerModifiable) handler);
		}
		else {
			tempHandler = getItemHandlerCopy(handler);
		}
		return ItemStack.areItemStacksEqual(stack, ItemHandlerHelper.insertItemStacked(tempHandler, stack.copy(), false));
	}

	public static IItemHandlerModifiable getItemHandlerModifiableCopy(IItemHandlerModifiable handler) {
		return new IItemHandlerModifiable() {
			@Override
			public int getSlots() {
				return handler.getSlots();
			}

			@Override
			public ItemStack getStackInSlot(int slot) {
				return handler.getStackInSlot(slot);
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				return handler.insertItem(slot, stack, simulate);
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				return handler.extractItem(slot, amount, simulate);
			}

			@Override
			public void setStackInSlot(int slot, ItemStack stack) {
				handler.setStackInSlot(slot, stack);
			}
		};
	}

	public static IItemHandler getItemHandlerCopy(IItemHandler handler) {
		return new IItemHandler() {

			@Override
			public int getSlots() {
				return handler.getSlots();
			}

			@Override
			public ItemStack getStackInSlot(int slot) {
				return handler.getStackInSlot(slot);
			}

			@Override
			public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
				return handler.insertItem(slot, stack, simulate);
			}

			@Override
			public ItemStack extractItem(int slot, int amount, boolean simulate) {
				return handler.extractItem(slot, amount, simulate);
			}

		};
	}

	public static IInventory getInventoryCopy(IInventory inventory) {
		return new IInventory() {
			@Override
			public String getName() {
				return inventory.getName();
			}

			@Override
			public boolean hasCustomName() {
				return inventory.hasCustomName();
			}

			@Override
			public ITextComponent getDisplayName() {
				return inventory.getDisplayName();
			}

			@Override
			public int getSizeInventory() {
				return inventory.getSizeInventory();
			}

			@Override
			public ItemStack getStackInSlot(int index) {
				return inventory.getStackInSlot(index);
			}

			@Override
			public ItemStack decrStackSize(int index, int count) {
				return inventory.decrStackSize(index, count);
			}

			@Override
			public ItemStack removeStackFromSlot(int index) {
				return inventory.removeStackFromSlot(index);
			}

			@Override
			public void setInventorySlotContents(int index, ItemStack stack) {
				inventory.setInventorySlotContents(index, stack);
			}

			@Override
			public int getInventoryStackLimit() {
				return inventory.getInventoryStackLimit();
			}

			@Override
			public void markDirty() {
				inventory.markDirty();
			}

			@Override
			public boolean isUseableByPlayer(EntityPlayer player) {
				return inventory.isUseableByPlayer(player);
			}

			@Override
			public void openInventory(EntityPlayer player) {
				inventory.openInventory(player);
			}

			@Override
			public void closeInventory(EntityPlayer player) {
				inventory.closeInventory(player);
			}

			@Override
			public boolean isItemValidForSlot(int index, ItemStack stack) {
				return inventory.isItemValidForSlot(index, stack);
			}

			@Override
			public int getField(int id) {
				return inventory.getField(id);
			}

			@Override
			public void setField(int id, int value) {
				inventory.setField(id, value);
			}

			@Override
			public int getFieldCount() {
				return inventory.getFieldCount();
			}

			@Override
			public void clear() {
				inventory.clear();
			}

		};
	}

	public static ItemStack insertMergeable(IInventory inventory, int slot, ItemStack stack, ItemStack existingStack) {
		int stackLimit = Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize());
		if (existingStack.stackSize >= stackLimit) {
			return stack;
		}
		if (stack.stackSize + existingStack.stackSize > stackLimit) {
			int stackDiff = stackLimit - existingStack.stackSize;
			existingStack.stackSize = stackLimit;
			stack.stackSize -= stackDiff;
			inventory.setInventorySlotContents(slot, existingStack);
			return stack;
		}
		existingStack.stackSize += stack.stackSize;
		inventory.setInventorySlotContents(slot, existingStack);
		return stackLimit >= stack.stackSize ? null : stack.splitStack(stack.stackSize - stackLimit);
	}

	public static ItemStack insertEmpty(IInventory inventory, int slot, ItemStack stack) {
		if (!inventory.isItemValidForSlot(slot, stack)) {
			return stack;
		}
		int stackLimit = inventory.getInventoryStackLimit();
		inventory.setInventorySlotContents(slot, stack.copy());
		return stackLimit >= stack.stackSize ? null : stack.splitStack(stack.stackSize - stackLimit);
	}

	public static ItemStack insertStack(TileEntity tile, ItemStack stack) {
		ItemStack returnStack = stack;
		if (ItemStack.areItemStacksEqual(stack, insertStack(tile, null, stack.copy()))) {
			return returnStack;
		}
		for (EnumFacing facing : EnumFacing.VALUES) {
			returnStack = insertStack(tile, facing, stack.copy());
			if (!ItemStack.areItemStacksEqual(stack, returnStack)) {
				break;
			}
		}
		return returnStack;
	}

	public static ItemStack insertStack(TileEntity tile, EnumFacing side, ItemStack stack) {
		return insertStack(tile, side, stack, false);
	}

	public static ItemStack insertStack(TileEntity tile, EnumFacing side, ItemStack stack, boolean simulate) {
		ItemStack newStack = stack;
		if (stack != null) {
			if (isItemHandler(tile, side)) {
				IItemHandler handler = getItemHandler(tile, side);
				if (handler != null) {
					newStack = ItemHandlerHelper.insertItemStacked(handler, stack.copy(), simulate);
				}
			}
			else {
				if (isInventory(tile)) {
					if (tile instanceof ISidedInventory) {
						newStack = insertItem((ISidedInventory) tile, stack.copy(), side);
					}
					else {
						newStack = insertItem((IInventory) tile, stack.copy());
					}
				}
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
