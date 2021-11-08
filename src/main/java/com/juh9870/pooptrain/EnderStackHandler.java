package com.juh9870.pooptrain;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.manager.EnderStorageManager;
import codechicken.enderstorage.storage.EnderItemStorage;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;

public class EnderStackHandler extends ItemStackHandler {
	protected Frequency frequency;
	protected boolean isClientSide;
	protected EnderItemStorage storage;
	protected int managerGeneration = PoopTrain.managerGeneration;

	public EnderStackHandler() {
		this(new Frequency(), true);
	}

	public EnderStackHandler(EnderItemStorage storage) {
		frequency = storage.freq;
		isClientSide = storage.manager.client;
	}

	public EnderStackHandler(Frequency frequency, boolean isClientSide) {
		this.frequency = frequency;
		this.isClientSide = isClientSide;
	}

	private EnderItemStorage storage() {
		if (storage == null || managerGeneration != PoopTrain.managerGeneration) {
			managerGeneration = PoopTrain.managerGeneration;
			storage = getStorage();
		}
		return storage;
	}

	public EnderItemStorage getStorage() {
		return EnderStorageManager.instance(isClientSide).getStorage(frequency, EnderItemStorage.TYPE);
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.put("Frequency", frequency.writeToNBT(new CompoundNBT()));
		nbt.putBoolean("Clientside", isClientSide);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		frequency.set(new Frequency(nbt.getCompound("Frequency")));
		isClientSide = nbt.getBoolean("Clientside");
		storage = null;
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		storage().setItem(slot, stack);
	}

	@Override
	public int getSlots() {
		return storage().getContainerSize();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		return storage().getItem(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {

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
		if (amount == 0)
			return ItemStack.EMPTY;

		validateSlotIndex(slot);

		ItemStack existing = getStackInSlot(slot);

		if (existing.isEmpty())
			return ItemStack.EMPTY;

		int toExtract = Math.min(amount, existing.getMaxStackSize());

		if (existing.getCount() <= toExtract) {
			if (!simulate) {
				setStackInSlot(slot, ItemStack.EMPTY);
				onContentsChanged(slot);
				return existing;
			} else {
				return existing.copy();
			}
		} else {
			if (!simulate) {
				setStackInSlot(slot, ItemHandlerHelper.copyStackWithSize(existing, existing.getCount() - toExtract));
				onContentsChanged(slot);
			}

			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		return storage().getMaxStackSize();
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return storage().canPlaceItem(slot, stack);
	}

	@Override
	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= getSlots())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
	}
}
