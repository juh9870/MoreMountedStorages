package com.juh9870.pooptrain.helpers;

import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class AdvancedStackHandlerRegistry extends ContraptionStorageRegistry {
	public static void register() {
		registerStorage(AdvancedStackHandlerRegistry.class, new AdvancedStackHandlerRegistry());
	}

	@Override
	public boolean useCustomHandler() {
		return true;
	}

	@Override
	public ItemStackHandler getHandler(TileEntity te) {
		return new AdvancedItemStackHandler();
	}

	@Override
	public ItemStackHandler deserializeHandler(CompoundNBT nbt) {
		AdvancedItemStackHandler handler = new AdvancedItemStackHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
