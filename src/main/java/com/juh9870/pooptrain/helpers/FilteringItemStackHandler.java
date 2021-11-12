package com.juh9870.pooptrain.helpers;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.ListNBT;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import java.util.Arrays;

public abstract class FilteringItemStackHandler extends AdvancedItemStackHandler {

	protected ItemStack[] filter;

	public FilteringItemStackHandler() {
		this(1, 64);
	}

	public FilteringItemStackHandler(IItemHandler handler) {
		super(handler);
		filter = new ItemStack[stacks.size()];
		Arrays.fill(filter, ItemStack.EMPTY);
	}

	public FilteringItemStackHandler(int size, int stackSize) {
		super(size, stackSize);
		filter = new ItemStack[stacks.size()];
		Arrays.fill(filter, ItemStack.EMPTY);
	}

	@Override
	public void setSize(int size) {
		super.setSize(size);
		ItemStack[] newFilters = new ItemStack[size];
		Arrays.fill(newFilters, ItemStack.EMPTY);
		System.arraycopy(this.filter, 0, newFilters, 0, Math.min(this.filter.length, size));
		this.filter = newFilters;
	}

	public void setFilter(int slot, ItemStack stack) {
		filter[slot] = stack.copy();
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return super.isItemValid(slot, stack) &&
				(filter[slot].isEmpty() || ItemHandlerHelper.canItemStacksStack(filter[slot], stack));
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		ListNBT l = new ListNBT();
		for (int i = 0; i < stacks.size(); i++) {
			if (!filter[i].isEmpty()) {
				CompoundNBT subTag = new CompoundNBT();
				subTag.putInt("Slot", i);
				filter[i].save(subTag);
				l.add(subTag);
			}
		}
		nbt.put("Filter", l);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		filter = new ItemStack[stacks.size()];
		Arrays.fill(filter, ItemStack.EMPTY);
		ListNBT l = nbt.getList("Filter", Constants.NBT.TAG_COMPOUND);
		for (int i = 0; i < l.size(); i++) {
			CompoundNBT tag = l.getCompound(i);
			ItemStack stack = ItemStack.of(tag);
			filter[tag.getInt("Slot")] = stack;
		}
	}
}
