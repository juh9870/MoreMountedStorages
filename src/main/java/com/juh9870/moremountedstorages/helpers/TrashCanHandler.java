package com.juh9870.moremountedstorages.helpers;

import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public abstract class TrashCanHandler extends SmartItemStackHandler {

    @Override
    public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        if (!isItemValid(slot, stack)) return stack;
        return ItemStack.EMPTY;
    }

    @Nonnull
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return ItemStack.EMPTY;
    }

    public int getSlotLimit(int slot) {
        return 2147483647;
    }

    @Nonnull
    public ItemStack getStackInSlot(int slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public int getPriority() {
        return PRIORITY_TRASH;
    }
}
