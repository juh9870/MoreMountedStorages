package com.juh9870.moremountedstorages.integrations.enderchests;

import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.items.ItemHandlerHelper;
import shetiphian.core.common.inventory.InventoryInternal;
import shetiphian.enderchests.Configuration;
import shetiphian.enderchests.common.misc.ChestHelper;
import shetiphian.enderchests.common.tileentity.TileEntityEnderChest;

import javax.annotation.Nonnull;

public class EnderChestHandler extends ContraptionItemStackHandler implements ContraptionStorageRegistry.IWorldRequiringHandler {
	private String code;
	private String owner;
	private World world;
	private IInventory storage;
	private boolean valid;
	private  int managerGeneration;

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

	private IInventory storage() {
		if (storage == null || managerGeneration != EnderChestsRegistry.managerGeneration) {
			storage = getStorage();
		}
		return storage;
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
		storage = getStorage();
		valid = true;
	}

	private InventoryInternal getStorage() {
		return ChestHelper.getChest(world, owner, code);
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		if (!valid(slot)) return;
		storage().setItem(slot, stack);
	}

	@Override
	public int getSlots() {
		return Configuration.UPGRADE_SETTINGS.chestSizeMax.get();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		if (!valid(slot)) return ItemStack.EMPTY;
		return storage().getItem(slot);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (!valid(slot)) return stack;
		if (stack.isEmpty()) {
			return ItemStack.EMPTY;
		} else if (!this.isItemValid(slot, stack)) {
			return stack;
		} else {
			this.validateSlotIndex(slot);
			ItemStack existing = getStackInSlot(slot);
			int limit = this.getStackLimit(slot, stack);
			if (!existing.isEmpty()) {
				if (!ItemHandlerHelper.canItemStacksStack(stack, existing)) {
					return stack;
				}

				limit -= existing.getCount();
			}

			if (limit <= 0) {
				return stack;
			} else {
				boolean reachedLimit = stack.getCount() > limit;
				if (!simulate) {
					if (existing.isEmpty()) {
						setStackInSlot(slot, reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, limit) : stack);
					} else {
						existing.grow(reachedLimit ? limit : stack.getCount());
					}

					this.onContentsChanged(slot);
				}

				return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
			}
		}
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

	private boolean valid(int slot) {
		return valid && slot < storage().getContainerSize();
	}

	@Override
	public int getSlotLimit(int slot) {
		if (!valid(slot)) return 0;
		return storage().getMaxStackSize();
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		return valid(slot) && storage().canPlaceItem(slot, stack);
	}

	@Override
	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= getSlots()) {
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + getSlots() + ")");
		}
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
