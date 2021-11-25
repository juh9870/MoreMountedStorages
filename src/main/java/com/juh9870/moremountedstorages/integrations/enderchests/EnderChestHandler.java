package com.juh9870.moremountedstorages.integrations.enderchests;

import com.juh9870.moremountedstorages.helpers.InventoryWrapperStackHandler;
import com.simibubi.create.api.contraption.ContraptionStorageRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import shetiphian.enderchests.Configuration;
import shetiphian.enderchests.common.misc.ChestHelper;
import shetiphian.enderchests.common.tileentity.TileEntityEnderChest;

public class EnderChestHandler extends InventoryWrapperStackHandler<IInventory> {
	private String code;
	private String owner;
	private World world;
	private boolean valid;
	private int managerGeneration;

	public EnderChestHandler() {
		code = "000";
		owner = "all";
		storage = new Inventory(Configuration.UPGRADE_SETTINGS.chestSizeMax.get());
		valid = false;
	}

	public EnderChestHandler(TileEntityEnderChest chest) {
		this.code = chest.getCode();
		this.owner = chest.getOwnerID();
		world = chest.getLevel();
		valid = true;
	}

	@Override
	public boolean addStorageToWorld(TileEntity te) {
		valid = false;
		return false;
	}

	@Override
	protected ContraptionStorageRegistry registry() {
		return EnderChestsRegistry.INSTANCE.get();
	}

	@Override
	public int getPriority() {
		return EnderChestsRegistry.CONFIG.getPriority();
	}

	@Override
	public void applyWorld(World world) {
		this.world = world;
		updateStorage();
		valid = true;
	}

	@Override
	protected boolean storageStillValid() {
		return EnderChestsRegistry.managerGeneration == managerGeneration;
	}

	@Override
	protected void updateStorage() {
		storage = ChestHelper.getChest(world, owner, code);
		managerGeneration = EnderChestsRegistry.managerGeneration;
	}


	@Override
	public int getSlots() {
		return Configuration.UPGRADE_SETTINGS.chestSizeMax.get();
	}


	@Override
	protected boolean valid(int slot) {
		return valid && slot < storage().getContainerSize();
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.putString("owner", owner);
		nbt.putString("code", code);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);
		owner = nbt.getString("owner");
		code = nbt.getString("code");
		valid = false;
	}
}
