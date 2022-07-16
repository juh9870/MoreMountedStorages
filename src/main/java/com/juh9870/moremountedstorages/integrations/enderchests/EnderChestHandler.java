package com.juh9870.moremountedstorages.integrations.enderchests;

import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.helpers.InventoryWrapperStackHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;
import shetiphian.enderchests.Configuration;
import shetiphian.enderchests.common.misc.ChestHelper;
import shetiphian.enderchests.common.tileentity.TileEntityEnderChest;

public class EnderChestHandler extends InventoryWrapperStackHandler<Container> {
	private String code;
	private String owner;
	private Level world;
	private boolean valid;
	private int managerGeneration;

	public EnderChestHandler() {
		code = "000";
		owner = "all";
		storage = new SimpleContainer(Configuration.UPGRADE_SETTINGS.chestSizeMax.get());
		valid = false;
	}

	public EnderChestHandler(TileEntityEnderChest chest) {
		this.code = chest.getCode();
		this.owner = chest.getOwnerID();
		world = chest.getLevel();
		valid = true;
	}

	@Override
	public boolean addStorageToWorld(BlockEntity te) {
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
	public void applyWorld(Level world) {
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
	public CompoundTag serializeNBT() {
		CompoundTag nbt = super.serializeNBT();
		nbt.putString("owner", owner);
		nbt.putString("code", code);
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);
		owner = nbt.getString("owner");
		code = nbt.getString("code");
		valid = false;
	}
}
