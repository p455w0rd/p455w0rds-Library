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
import net.minecraft.util.NonNullList;
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
			NonNullList<ItemStack> armorStack = player.inventory.armorInventory;
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
			return armorStack.get(slotNum);
		}
		return ItemStack.EMPTY;
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
					inventorySlots.get(index).putStack(ItemStack.EMPTY);
					return true;
				}
			}
		}
		else if (isInInventory(index)) {
			for (int i = 0; i <= 8; i++) {
				Slot possiblyOpenSlot = inventorySlots.get(i);
				if (!possiblyOpenSlot.getHasStack()) {
					possiblyOpenSlot.putStack(itemStackIn);
					inventorySlots.get(index).putStack(ItemStack.EMPTY);
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
			if ((s != null) && (s.getStack().isEmpty())) {
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
		if (stack.isEmpty()) {
			return true;
		}
		int openSlot = -1;
		for (int i = startIndex; i <= endIndex; i++) {
			if (ItemStack.areItemStacksEqual(stack, inventory[i]) && inventory[i].getMaxStackSize() > inventory[i].getCount()) {
				int hold = inventory[i].getMaxStackSize() - inventory[i].getCount();
				if (hold >= stack.getCount()) {
					inventory[i].grow(stack.getCount());
					stack = ItemStack.EMPTY;
					return true;
				}
				else {
					stack.shrink(hold);
					inventory[i].grow(hold);
				}
			}
			else if (inventory[i].isEmpty() && openSlot == -1) {
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
		if (stack.isEmpty() || inventory == null) {
			return ItemStack.EMPTY;
		}
		int stackSize = stack.getCount();
		if (inventory instanceof ISidedInventory) {
			ISidedInventory sidedInv = (ISidedInventory) inventory;
			int slots[] = sidedInv.getSlotsForFace(side);

			if (slots == null) {
				return stack;
			}
			for (int i = 0; i < slots.length && !stack.isEmpty(); i++) {
				if (sidedInv.canInsertItem(slots[i], stack, side)) {
					ItemStack existingStack = inventory.getStackInSlot(slots[i]);
					if (ItemStack.areItemStacksEqual(stack, existingStack)) {
						stack = insertMergeable(sidedInv, slots[i], stack, existingStack);
					}
				}
			}
			for (int i = 0; i < slots.length && !stack.isEmpty(); i++) {
				if (inventory.getStackInSlot(slots[i]).isEmpty() && sidedInv.canInsertItem(slots[i], stack, side)) {
					stack = insertEmpty(sidedInv, slots[i], stack);
				}
			}
		}
		else {
			int invSize = inventory.getSizeInventory();
			for (int i = 0; i < invSize && !stack.isEmpty(); i++) {
				ItemStack existingStack = inventory.getStackInSlot(i);
				if (ItemStack.areItemStacksEqual(stack, existingStack)) {
					stack = insertMergeable(inventory, i, stack, existingStack);
				}
			}
			for (int i = 0; i < invSize && !stack.isEmpty(); i++) {
				if (inventory.getStackInSlot(i).isEmpty()) {
					stack = insertEmpty(inventory, i, stack);
				}
			}
		}
		if (stack.isEmpty() || stack.getCount() != stackSize) {
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
		if (stack.isEmpty()) {
			return false;
		}
		IInventory tempInventory = inventory instanceof ISidedInventory ? getSidedInventoryCopy((ISidedInventory) inventory) : getInventoryCopy(inventory);
		if (!(inventory instanceof ISidedInventory)) {
			return !ItemUtils.areStacksSameSize(stack.copy(), insertItem(tempInventory, stack.copy()));
		}
		else {
			if (isInventoryValidForItem((ISidedInventory) tempInventory, stack.copy(), null)) {
				return true;
			}
			for (EnumFacing facing : EnumFacing.VALUES) {
				if (isInventoryValidForItem((ISidedInventory) tempInventory, stack.copy(), facing)) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean isInventoryValidForItem(ISidedInventory inv, ItemStack stack, @Nullable EnumFacing facing) {
		boolean canInsert = false;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.canInsertItem(i, stack, facing)) {
				canInsert = true;
				break;
			}
		}
		return isInventoryValidForItem(inv, stack) && inv.getSlotsForFace(facing).length > 0 && canInsert;
	}

	public static boolean isInventoryValidForItem(IInventory inv, ItemStack stack) {
		boolean hasAvailableSlot = false;
		for (int i = 0; i < inv.getSizeInventory(); i++) {
			if (inv.getStackInSlot(i).isEmpty() && inv.isItemValidForSlot(i, stack)) {
				hasAvailableSlot = true;
				break;
			}
			else {
				ItemStack slotStack = inv.getStackInSlot(i);
				if (ItemStack.areItemStacksEqual(stack, slotStack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
					hasAvailableSlot = true;
					break;
				}
			}
		}
		return hasAvailableSlot;
	}

	public static boolean isInventoryValidForItem(IItemHandler handler, ItemStack stack) {
		boolean hasAvailableSlot = false;
		for (int i = 0; i < handler.getSlots(); i++) {
			if (handler.getStackInSlot(i).isEmpty()) {
				hasAvailableSlot = true;
				break;
			}
			else {
				ItemStack slotStack = handler.getStackInSlot(i);
				if (ItemStack.areItemStacksEqual(stack, slotStack) && slotStack.getCount() < slotStack.getMaxStackSize()) {
					hasAvailableSlot = true;
					break;
				}
			}
		}
		return hasAvailableSlot;
	}

	public static boolean hasRoomForStack(IItemHandler handler, ItemStack stack) {
		if (stack.isEmpty()) {
			return false;
		}
		IItemHandler tempHandler = null;
		if (handler instanceof IItemHandlerModifiable) {
			tempHandler = getItemHandlerModifiableCopy((IItemHandlerModifiable) handler);
		}
		else {
			tempHandler = getItemHandlerCopy(handler);
		}
		return isInventoryValidForItem(tempHandler, stack);
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

			@Override
			public int getSlotLimit(int slot) {
				return handler.getSlotLimit(slot);
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

			@Override
			public int getSlotLimit(int slot) {
				return handler.getSlotLimit(slot);
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
			public boolean isUsableByPlayer(EntityPlayer player) {
				return inventory.isUsableByPlayer(player);
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

			@Override
			public boolean isEmpty() {
				return inventory.isEmpty();
			}

		};
	}

	public static ISidedInventory getSidedInventoryCopy(ISidedInventory inventory) {
		return new ISidedInventory() {
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
			public boolean isUsableByPlayer(EntityPlayer player) {
				return inventory.isUsableByPlayer(player);
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

			@Override
			public int[] getSlotsForFace(EnumFacing side) {
				return inventory.getSlotsForFace(side);
			}

			@Override
			public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
				return inventory.canInsertItem(index, itemStackIn, direction);
			}

			@Override
			public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
				return inventory.canExtractItem(index, stack, direction);
			}

			@Override
			public boolean isEmpty() {
				return inventory.isEmpty();
			}

		};
	}

	public static ItemStack insertMergeable(IInventory inventory, int slot, ItemStack stack, ItemStack existingStack) {
		int stackLimit = Math.min(inventory.getInventoryStackLimit(), stack.getMaxStackSize());
		if (existingStack.getCount() >= stackLimit) {
			return stack;
		}
		if (stack.getCount() + existingStack.getCount() > stackLimit) {
			int stackDiff = stackLimit - existingStack.getCount();
			existingStack.setCount(stackLimit);
			stack.shrink(stackDiff);
			inventory.setInventorySlotContents(slot, existingStack);
			return stack;
		}
		existingStack.grow(stack.getCount());
		inventory.setInventorySlotContents(slot, existingStack);
		return stackLimit >= stack.getCount() ? ItemStack.EMPTY : stack.splitStack(stack.getCount() - stackLimit);
	}

	public static ItemStack insertEmpty(IInventory inventory, int slot, ItemStack stack) {
		if (!inventory.isItemValidForSlot(slot, stack)) {
			return stack;
		}
		int stackLimit = inventory.getInventoryStackLimit();
		inventory.setInventorySlotContents(slot, stack.copy());
		return stackLimit >= stack.getCount() ? ItemStack.EMPTY : stack.splitStack(stack.getCount() - stackLimit);
	}

	public static ItemStack insertStack(TileEntity tile, ItemStack stack) {
		ItemStack returnStack = stack;
		if (!ItemStack.areItemStacksEqual(returnStack, stack = insertStack(tile, null, stack.copy()))) {
			return stack;
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
		if (!stack.isEmpty()) {
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
		if (stack.isEmpty()) {
			return false;
		}
		for (int slot = 0; slot < inv.getSlots(); slot++) {
			ItemStack stackInSlot = inv.getStackInSlot(slot);
			int m;
			if (!stackInSlot.isEmpty()) {
				if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
					continue;
				}
				m = Math.min(stack.getMaxStackSize(), inv.getStackInSlot(slot).getMaxStackSize()) - stackInSlot.getCount();
				if (stack.getCount() < m) {
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
		if (stack.isEmpty() || inv.getSizeInventory() <= 0) {
			return ItemStack.EMPTY;
		}
		for (int slot = 0; slot < inv.getSizeInventory(); ++slot) {
			if (!inv.isItemValidForSlot(slot, stack)) {
				return stack;
			}
			ItemStack stackInSlot = inv.getStackInSlot(slot);

			int m;
			if (!stackInSlot.isEmpty()) {
				if (!ItemHandlerHelper.canItemStacksStack(stack, stackInSlot)) {
					return stack;
				}

				m = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit()) - stackInSlot.getCount();

				if (stack.getCount() <= m) {
					if (!simulate) {
						ItemStack copy = stack.copy();
						copy.grow(stackInSlot.getCount());
						inv.setInventorySlotContents(slot, copy);
						inv.markDirty();
					}
					continue;
				}
				else {
					// copy the stack to not modify the original one
					stack = stack.copy();
					if (!simulate) {
						ItemStack copy = stack.splitStack(m);
						copy.grow(stackInSlot.getCount());
						inv.setInventorySlotContents(slot, copy);
						inv.markDirty();
						return stack;
					}
					else {
						stack.shrink(m);
						return stack;
					}
				}
			}
			else {
				m = Math.min(stack.getMaxStackSize(), inv.getInventoryStackLimit());
				if (m < stack.getCount()) {
					// copy the stack to not modify the original one
					stack = stack.copy();
					if (!simulate) {
						inv.setInventorySlotContents(slot, stack.splitStack(m));
						inv.markDirty();
						return stack;
					}
					else {
						stack.shrink(m);
						return stack;
					}
				}
				else {
					if (!simulate) {
						inv.setInventorySlotContents(slot, stack);
						inv.markDirty();
					}
					return ItemStack.EMPTY;
				}
			}
		}
		return stack;
	}

	@Nullable
	public static ItemStack addItem(IInventory inv, ItemStack stack) {
		ItemStack itemstack = stack.copy();

		for (int i = 0; i < inv.getSizeInventory(); ++i) {
			ItemStack itemstack1 = inv.getStackInSlot(i);

			if (itemstack1.isEmpty()) {
				inv.setInventorySlotContents(i, itemstack);
				inv.markDirty();
				return ItemStack.EMPTY;
			}

			if (ItemStack.areItemsEqual(itemstack1, itemstack)) {
				int j = Math.min(inv.getInventoryStackLimit(), itemstack1.getMaxStackSize());
				int k = Math.min(itemstack.getCount(), j - itemstack1.getCount());

				if (k > 0) {
					itemstack1.grow(k);
					itemstack.shrink(k);

					if (itemstack.getCount() <= 0) {
						inv.markDirty();
						return ItemStack.EMPTY;
					}
				}
			}
		}

		if (itemstack.getCount() != stack.getCount()) {
			inv.markDirty();
		}

		return itemstack;
	}

}
