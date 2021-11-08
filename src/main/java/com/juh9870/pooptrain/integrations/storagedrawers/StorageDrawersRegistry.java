package com.juh9870.pooptrain.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class StorageDrawersRegistry extends ContraptionStorageRegistry {
	public static void register() {
		registerStorages(new StorageDrawersRegistry(),
				TileEntityDrawersStandard.class,

				TileEntityDrawersStandard.Slot1.class,
				TileEntityDrawersStandard.Slot2.class,
				TileEntityDrawersStandard.Slot4.class
		);
	}

	@Override
	public boolean useCustomHandler() {
		return true;
	}

	@Override
	public boolean prepareStorageForContraption(TileEntity te) {
		TileEntityDrawersStandard drawer = (TileEntityDrawersStandard) te;
		return drawer.isGroupValid();
	}

	@Override
	public ItemStackHandler getHandler(TileEntity te) {
		return new StorageDrawerHandler((TileEntityDrawersStandard) te);
	}

	@Override
	public boolean addStorageToWorld(TileEntity te, ItemStackHandler handler) {
		TileEntityDrawersStandard drawer = (TileEntityDrawersStandard) te;
		StorageDrawerHandler drawerHandler = (StorageDrawerHandler) handler;
		drawerHandler.copyItemsTo(drawer);
		return false;
	}

	@Override
	public ItemStackHandler deserializeHandler(CompoundNBT nbt) {
		StorageDrawerHandler handler = new StorageDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
