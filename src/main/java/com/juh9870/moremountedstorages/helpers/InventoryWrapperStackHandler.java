package com.juh9870.moremountedstorages.helpers;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class InventoryWrapperStackHandler<T extends Container> extends SmartItemStackHandler {
	protected T storage;

	protected T storage() {
		if (storage == null || !storageStillValid()) updateStorage();
		return storage;
	}

	protected boolean storageStillValid() {
		return true;
	}

	protected abstract void updateStorage();

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		if (!valid(slot)) return;
		storage().setItem(slot, stack);
	}

	@Override
	public int getSlots() {
		return storage().getContainerSize();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (!valid(slot)) return ItemStack.EMPTY;
		return storage().getItem(slot);
	}

	@Override
	public int getSlotLimit(int slot) {
		if (!valid(slot)) return 0;
		return storage().getMaxStackSize();
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return valid(slot) && storage().canPlaceItem(slot, stack);
	}
}
