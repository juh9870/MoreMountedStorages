package com.juh9870.pooptrain.integrations.enderstorage;

import codechicken.enderstorage.tile.TileEnderChest;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class EnderStorageRegistry extends ContraptionStorageRegistry {
	public static void register() {
		registerStorage(TileEnderChest.class, new EnderStorageRegistry());
	}

	@Override
	public boolean useCustomHandler() {
		return true;
	}

	@Override
	public ItemStackHandler getHandler(TileEntity te) {
		TileEnderChest chest = (TileEnderChest) te;
		return new EnderStackHandler(chest.getStorage());
	}

	@Override
	public ItemStackHandler deserializeHandler(CompoundNBT nbt) {
		EnderStackHandler handler = new EnderStackHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
