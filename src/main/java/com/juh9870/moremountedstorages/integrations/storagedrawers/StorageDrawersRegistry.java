package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ObjectHolder;

public class StorageDrawersRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance("storagedrawers:drawer");


	@Override
	public boolean canUseAsStorage(TileEntity te) {
		TileEntityDrawersStandard drawer = (TileEntityDrawersStandard) te;
		return drawer.isGroupValid() && Config.STORAGE_DRAWERS.get();
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.STANDARD_DRAWERS_1,
				ModBlocks.Tile.STANDARD_DRAWERS_2,
				ModBlocks.Tile.STANDARD_DRAWERS_4,
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		return new StorageDrawerHandler((TileEntityDrawersStandard) te);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		StorageDrawerHandler handler = new StorageDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
