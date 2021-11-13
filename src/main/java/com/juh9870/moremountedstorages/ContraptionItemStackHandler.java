package com.juh9870.moremountedstorages;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.NonNullList;
import net.minecraftforge.items.ItemStackHandler;

public abstract class ContraptionItemStackHandler extends ItemStackHandler {

	public ContraptionItemStackHandler() {
	}

	public ContraptionItemStackHandler(int size) {
		super(size);
	}

	public ContraptionItemStackHandler(NonNullList<ItemStack> stacks) {
		super(stacks);
	}

	/**
	 * Performs manipulations on Tile Entity when it's being added to the world after contraption disassembly and returns false if default logic of copying items from handler to inventory should be skipped;
	 *
	 * @param te Tile Entity being added to the world
	 * @return false if default create logic should be skipped
	 */
	public boolean addStorageToWorld(TileEntity te) {
		return true;
	}


	/**
	 * Returns associated {@link ContraptionStorageRegistry}
	 *
	 * @return associated registry
	 */
	protected abstract ContraptionStorageRegistry registry();

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		nbt.putString(ContraptionStorageRegistry.REGISTRY_NAME, registry().getRegistryName().toString());
		return nbt;
	}
}
