package com.juh9870.moremountedstorages.helpers;

import com.simibubi.create.api.contraption.ContraptionItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 * ItemStackHandler but uses slot access methods instead of directly referencing this.storage, also includes check for slot validity
 */
public abstract class SmartItemStackHandler extends ContraptionItemStackHandler {

	public SmartItemStackHandler() {
	}

	public SmartItemStackHandler(int size) {
		super(size);
	}

	public SmartItemStackHandler(NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	protected boolean valid(int slot) {
		return true;
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (!valid(slot)) return stack;
		if (stack.isEmpty())
			return ItemStack.EMPTY;

		if (!isItemValid(slot, stack))
			return stack;

		validateSlotIndex(slot);

		ItemStack existing = getStackInSlot(slot);

		int limit = getStackLimit(slot, stack);

		if (!existing.isEmpty()) {
			if (!ItemHandlerHelper.canItemStacksStack(stack, existing))
				return stack;

			limit -= existing.getCount();
		}

		if (limit <= 0)
			return stack;

		boolean reachedLimit = stack.getCount() > limit;

		if (!simulate) {
			if (existing.isEmpty()) {
				setStackInSlot(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
			} else {
				existing.grow(reachedLimit ? limit : stack.getCount());
			}
			onContentsChanged(slot);
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (!valid(slot)) return ItemStack.EMPTY;
		if (amount == 0) {
			return ItemStack.EMPTY;
		} else {
			this.validateSlotIndex(slot);
			ItemStack existing = getStackInSlot(slot);
			if (existing.isEmpty()) {
				return ItemStack.EMPTY;
			} else {
				int toExtract = Math.min(amount, existing.getMaxStackSize());
				if (existing.getCount() <= toExtract) {
					if (!simulate) {
						setStackInSlot(slot, ItemStack.EMPTY);
						this.onContentsChanged(slot);
						return existing;
					} else {
						return existing.copy();
					}
				} else {
					if (!simulate) {
						setStackInSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
						this.onContentsChanged(slot);
					}

					return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
				}
			}
		}
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return valid(slot) && super.isItemValid(slot, stack);
	}

	@Override
	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= getSlots()) {
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
		}
	}
}
