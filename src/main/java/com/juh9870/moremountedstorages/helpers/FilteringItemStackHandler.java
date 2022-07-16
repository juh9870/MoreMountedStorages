package com.juh9870.moremountedstorages.helpers;

import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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

	public FilteringItemStackHandler setFilter(int slot, ItemStack stack) {
		filter[slot] = stack.copy();
		return this;
	}

	@Override
	public FilteringItemStackHandler setVoiding(boolean voiding) {
		return (FilteringItemStackHandler) super.setVoiding(voiding);
	}

	@Override
	public FilteringItemStackHandler setVoiding(int slot, boolean voiding) {
		return (FilteringItemStackHandler) super.setVoiding(slot, voiding);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return super.isItemValid(slot, stack) &&
				(filter[slot].isEmpty() || ItemHandlerHelper.canItemStacksStack(filter[slot], stack));
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = super.serializeNBT();
		ListTag l = new ListTag();
		for (int i = 0; i < stacks.size(); i++) {
			if (!filter[i].isEmpty()) {
				CompoundTag subTag = new CompoundTag();
				subTag.putInt("Slot", i);
				filter[i].save(subTag);
				l.add(subTag);
			}
		}
		nbt.put("Filter", l);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		filter = new ItemStack[stacks.size()];
		Arrays.fill(filter, ItemStack.EMPTY);
		ListTag l = nbt.getList("Filter", Tag.TAG_COMPOUND);
		for (int i = 0; i < l.size(); i++) {
			CompoundTag tag = l.getCompound(i);
			ItemStack stack = ItemStack.of(tag);
			filter[tag.getInt("Slot")] = stack;
		}
	}
}
