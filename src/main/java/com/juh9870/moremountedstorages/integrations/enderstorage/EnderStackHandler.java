package com.juh9870.moremountedstorages.integrations.enderstorage;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.manager.EnderStorageManager;
import codechicken.enderstorage.storage.EnderItemStorage;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.helpers.InventoryWrapperStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;

public class EnderStackHandler extends InventoryWrapperStackHandler<EnderItemStorage> {
	protected Frequency frequency;
	protected boolean isClientSide;
	protected int managerGeneration = EnderStorageRegistry.managerGeneration;
	protected boolean valid = true;

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

	@Override
	protected boolean storageStillValid() {
		return managerGeneration == EnderStorageRegistry.managerGeneration;
	}

	@Override
	protected void updateStorage() {
		storage = EnderStorageManager.instance(isClientSide).getStorage(frequency, EnderItemStorage.TYPE);
		managerGeneration = EnderStorageRegistry.managerGeneration;
	}

	@Override
	public CompoundTag serializeNBT() {
		CompoundTag nbt = super.serializeNBT();
		nbt.put("Frequency", frequency.writeToNBT(new CompoundTag()));
		nbt.putBoolean("Clientside", isClientSide);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		frequency.set(new Frequency(nbt.getCompound("Frequency")));
		isClientSide = nbt.getBoolean("Clientside");
		storage = null;
	}

	@Override
	protected boolean valid(int slot) {
		return valid;
	}

	@Override
	public boolean addStorageToWorld(BlockEntity te) {
		valid = false;
		return false;
	}

	@Override
	protected ContraptionStorageRegistry registry() {
		return EnderStorageRegistry.INSTANCE.get();
	}

	@Override
	public int getPriority() {
		return EnderStorageRegistry.CONFIG.getPriority();
	}
}
