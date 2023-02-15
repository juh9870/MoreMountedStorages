package com.juh9870.moremountedstorages.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.IItemHandler;

import javax.annotation.Nonnull;
import java.util.Arrays;

/**
 * ItemStackHandler with stack size customization and voiding options
 */
public abstract class AdvancedItemStackHandler extends SmartItemStackHandler {
    protected int[] stackSizes;
    protected boolean ignoreItemStackSize;
    protected boolean[] voiding;

    public AdvancedItemStackHandler() {
        this(1, 64);
    }

    public AdvancedItemStackHandler(IItemHandler handler) {
        super(handler.getSlots());
        this.stackSizes = new int[stacks.size()];
        this.voiding = new boolean[stacks.size()];
        for (int i = 0; i < stacks.size(); i++) {
            this.stackSizes[i] = handler.getSlotLimit(i);
            this.stacks.set(i, handler.getStackInSlot(i));
        }
    }

    public AdvancedItemStackHandler(int size, int stackSize) {
        super(size);
        this.stackSizes = new int[size];
        this.voiding = new boolean[size];
        Arrays.fill(stackSizes, stackSize);
    }

    public AdvancedItemStackHandler setIgnoreItemStackSize(boolean ignoreItemStackSize) {
        this.ignoreItemStackSize = ignoreItemStackSize;
        return this;
    }

    public AdvancedItemStackHandler setVoiding(boolean voiding) {
        Arrays.fill(this.voiding, voiding);
        return this;
    }

    public AdvancedItemStackHandler setVoiding(int slot, boolean voiding) {
        this.voiding[slot] = voiding;
        return this;
    }

    public void simpleOverwrite(IItemHandler target) {
        if (this.stacks.size() != target.getSlots()) {
            throw new IllegalArgumentException("Slots count differes from the target");
        }
        copyItemsOver(this, target, getSlots(), 0, 0);
    }

    @Override
    public void setSize(int size) {
        super.setSize(size);
        int[] newSizes = new int[size];
        Arrays.fill(newSizes, 64);
        System.arraycopy(this.stackSizes, 0, newSizes, 0, Math.min(this.stackSizes.length, size));
        this.stackSizes = newSizes;

        boolean[] newVoids = new boolean[size];
        Arrays.fill(newVoids, false);
        System.arraycopy(this.voiding, 0, newVoids, 0, Math.min(this.voiding.length, size));
        this.voiding = newVoids;
    }

    @Override
    public int getSlotLimit(int slot) {
        return voiding[slot] ? Integer.MAX_VALUE : stackSizes[slot];
    }

    @Override
    protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
        return voiding[slot] ? Integer.MAX_VALUE : getActualSlotSize(slot, stack);
    }

    protected int getActualSlotSize(int slot, @Nonnull ItemStack stack) {
        return ignoreItemStackSize ? stackSizes[slot] : Math.min(stackSizes[slot], (int) (stackSizes[slot] * (stack.getMaxStackSize() / 64f)));
    }

    @Override
    public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
        return stackSizes[slot] > 0;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
        ItemStack returnStack = super.insertItem(slot, stack, simulate);
        int actualLimit = getActualSlotSize(slot, stack);
        if (stacks.get(slot).getCount() > actualLimit) {
            stacks.get(slot).setCount(actualLimit);
        }
        return returnStack;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putIntArray("StackSizes", stackSizes);
        nbt.putBoolean("IgnoreItemStackSizes", ignoreItemStackSize);
        int[] voids = new int[voiding.length];
        for (int i = 0; i < voiding.length; i++) {
            voids[i] = voiding[i] ? 1 : 0;
        }
        nbt.putIntArray("Voiding", voids);
        // Item stacks are stored in byte, so extra count is lost, and we have
        // to serialize them separately
        int[] itemCounts = new int[stacks.size()];
        for (int i = 0; i < stacks.size(); i++) {
            itemCounts[i] = stacks.get(i).getCount();
        }
        nbt.putIntArray("ItemCounts", itemCounts);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        stackSizes = nbt.getIntArray("StackSizes");
        ignoreItemStackSize = nbt.getBoolean("IgnoreItemStackSizes");
        int[] voids = nbt.getIntArray("Voiding");
        voiding = new boolean[voids.length];
        for (int i = 0; i < voids.length; i++) {
            voiding[i] = voids[i] == 1;
        }
        // Read comment on line 117
        int[] itemCounts = nbt.getIntArray("ItemCounts");
        for (int i = 0; i < stacks.size(); i++) {
            stacks.get(i).setCount(itemCounts[i]);
        }
    }
}
