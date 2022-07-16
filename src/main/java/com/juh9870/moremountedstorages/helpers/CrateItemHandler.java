package com.juh9870.moremountedstorages.helpers;

import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;

import javax.annotation.ParametersAreNonnullByDefault;


@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public abstract class CrateItemHandler extends SmartItemStackHandler {
	private int allowedAmount = 0;

	public CrateItemHandler() {
		super(32);
	}

	public CrateItemHandler(int allowedAmount) {
		this();
		this.allowedAmount = allowedAmount;
	}

	public int getAllowedAmount() {
		return allowedAmount;
	}

	public CrateItemHandler setAllowedAmount(int allowedAmount) {
		this.allowedAmount = allowedAmount;
		return this;
	}

	@Override
	public int getPriority() {
		return 0;
	}

	@Override
	public int getSlotLimit(int slot) {
		if (slot < getAllowedAmount() / 64)
			return super.getSlotLimit(slot);
		else if (slot == getAllowedAmount() / 64)
			return getAllowedAmount() % 64;
		return 0;
	}

	@Override
	public boolean isItemValid(int slot, ItemStack stack) {
		if (slot > allowedAmount / 64)
			return false;
		return super.isItemValid(slot, stack);
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = super.serializeNBT();
		nbt.putInt("AllowedAmount", getAllowedAmount());
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		allowedAmount = nbt.getInt("AllowedAmount");
	}
}