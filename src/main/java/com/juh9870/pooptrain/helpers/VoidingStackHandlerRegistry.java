package com.juh9870.pooptrain.helpers;

import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class VoidingStackHandlerRegistry extends ContraptionStorageRegistry {
	public static void register() {
		registerStorage(VoidingStackHandlerRegistry.class, new VoidingStackHandlerRegistry());
	}

	@Override
	public boolean useCustomHandler() {
		return true;
	}

	@Override
	public ItemStackHandler getHandler(TileEntity te) {
		return new VoidingItemStackHandler();
	}

	@Override
	public ItemStackHandler deserializeHandler(CompoundNBT nbt) {
		VoidingItemStackHandler handler = new VoidingItemStackHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
