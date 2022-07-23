package com.juh9870.moremountedstorages.helpers;

import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.integrations.trashcans.TrashCansRegistry;
import com.supermartijn642.trashcans.TrashCanTile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;

import javax.annotation.Nonnull;

public abstract class TrashCanHandler extends SmartItemStackHandler {

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		if (!isItemValid(slot, stack)) return stack;
		return ItemStack.EMPTY;
	}

	@Nonnull
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		return ItemStack.EMPTY;
	}

	public int getSlotLimit(int slot) {
		return 2147483647;
	}

	@Nonnull
	public ItemStack getStackInSlot(int slot) {
		return ItemStack.EMPTY;
	}

	@Override
	public int getPriority() {
		return PRIORITY_TRASH;
	}

	@Override
	public boolean addStorageToWorld(BlockEntity te) {
		// workaround:
		// fixed client trash can not showing filters after being disassembled from a contraption
		// by forcing server to sync nbt with clients
		if (te instanceof TrashCanTile trashCanTile) {
			trashCanTile.dataChanged();
		}
		return super.addStorageToWorld(te);
	}
}
