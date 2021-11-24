package com.juh9870.moremountedstorages.integrations.dimstorage;

import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.helpers.InventoryWrapperStackHandler;
import edivad.dimstorage.api.Frequency;
import edivad.dimstorage.manager.DimStorageManager;
import edivad.dimstorage.storage.DimChestStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;

public class DimStorageStackHandler extends InventoryWrapperStackHandler<DimChestStorage> {
	protected Frequency frequency;
	protected boolean isClientSide;
	protected int managerGeneration = DimStorageRegistry.managerGeneration;
	protected boolean valid = true;

	public DimStorageStackHandler() {
		this(new Frequency(), true);
	}

	public DimStorageStackHandler(DimChestStorage storage) {
		frequency = storage.freq;
		isClientSide = !storage.manager.isServer();
	}

	public DimStorageStackHandler(Frequency frequency, boolean isClientSide) {
		this.frequency = frequency;
		this.isClientSide = isClientSide;
	}

	@Override
	protected boolean storageStillValid() {
		return managerGeneration == DimStorageRegistry.managerGeneration;
	}

	@Override
	protected void updateStorage() {
		storage = (DimChestStorage) DimStorageManager.instance(isClientSide).getStorage(frequency, "item");
		managerGeneration = DimStorageRegistry.managerGeneration;
	}


	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.put("Frequency", frequency.serializeNBT());
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
	protected boolean valid(int slot) {
		return valid;
	}

	@Override
	public boolean addStorageToWorld(TileEntity te) {
		valid = false;
		return false;
	}

	@Override
	protected ContraptionStorageRegistry registry() {
		return DimStorageRegistry.INSTANCE.get();
	}

	@Override
	public int getPriority() {
		return DimStorageRegistry.CONFIG.getPriority();
	}
}
