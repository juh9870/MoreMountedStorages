package com.juh9870.pooptrain.helpers;

import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;

public class VoidingItemStackHandler extends AdvancedItemStackHandler {
	protected int[] stackSizes;

	public VoidingItemStackHandler() {
		super();
	}

	public VoidingItemStackHandler(IItemHandler handler) {
		super(handler);
	}

	public VoidingItemStackHandler(IItemHandler handler, boolean ignoreItemStackSize) {
		super(handler, ignoreItemStackSize);
	}

	public VoidingItemStackHandler(int size, int stackSize, boolean ignoreItemStackSize) {
		super(size, stackSize, ignoreItemStackSize);
	}

	@Override
	protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
		return Integer.MAX_VALUE;
	}

	@Override
	public int getSlotLimit(int slot) {
		return Integer.MAX_VALUE;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		ItemStack returnStack = super.insertItem(slot, stack, simulate);
		int superLimit = super.getStackLimit(slot, stack);
		if (stacks.get(slot).getCount() > superLimit) {
			stacks.get(slot).setCount(superLimit);
		}
		return returnStack;
	}

	protected Class<?> getStorageClass() {
		return VoidingStackHandlerRegistry.class;
	}
}
