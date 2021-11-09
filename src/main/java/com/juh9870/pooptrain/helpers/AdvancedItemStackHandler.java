package com.juh9870.pooptrain.helpers;

import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

public class AdvancedItemStackHandler extends ItemStackHandler {
	protected int[] stackSizes;
	protected boolean ignoreItemStackSize = false;

	public AdvancedItemStackHandler() {
		this(1, 64, false);
	}

	public AdvancedItemStackHandler(IItemHandler handler) {
		this(handler,false);
	}
	public AdvancedItemStackHandler(IItemHandler handler, boolean ignoreItemStackSize) {
		super(handler.getSlots());
		this.stackSizes = new int[stacks.size()];
		for (int i = 0; i < stacks.size(); i++) {
			this.stackSizes[i] = handler.getSlotLimit(i);
			this.stacks.set(i, handler.getStackInSlot(i));
		}
		this.ignoreItemStackSize = ignoreItemStackSize;
	}

	public AdvancedItemStackHandler(int size, int stackSize, boolean ignoreItemStackSize) {
		super(size);
		this.stackSizes = new int[size];
		Arrays.fill(stackSizes, stackSize);
		this.ignoreItemStackSize = ignoreItemStackSize;
	}

	public void simpleOverwrite(IItemHandler target) {
		if (this.stacks.size() != target.getSlots()) {
			throw new IllegalArgumentException("Slots count differes from the target");
		}

		boolean canClear = target instanceof IItemHandlerModifiable;
		for (int i = 0; i < this.stacks.size(); i++) {
			if (canClear) {
				((IItemHandlerModifiable) target).setStackInSlot(i, getStackInSlot(i));
				continue;
			}
			target.extractItem(i, Integer.MAX_VALUE, false);
			target.insertItem(i, getStackInSlot(i), false);
		}
	}

	@Override
	public void setSize(int size) {
		super.setSize(size);
		int[] newSizes = new int[size];
		Arrays.fill(newSizes, 64);
		System.arraycopy(this.stackSizes, 0, newSizes, 0, Math.min(this.stackSizes.length, size));
		this.stackSizes = newSizes;
	}

	@Override
	public int getSlotLimit(int slot) {
		return stackSizes[slot];
	}

	@Override
	protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
		return ignoreItemStackSize ? stackSizes[slot] : Math.min(stackSizes[slot], (int) (stackSizes[slot] * (stack.getMaxStackSize() / 64f)));
	}

	protected Class<?> getStorageClass() {
		return AdvancedStackHandlerRegistry.class;
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		ContraptionStorageRegistry.serializeClassName(nbt, getStorageClass());
		nbt.putIntArray("StackSizes", stackSizes);
		nbt.putBoolean("IgnoreItemStackSizes", ignoreItemStackSize);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		stackSizes = nbt.getIntArray("StackSizes");
		ignoreItemStackSize = nbt.getBoolean("IgnoreItemStackSizes");
	}
}
