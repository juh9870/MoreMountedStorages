package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

import static com.juh9870.moremountedstorages.ContraptionItemStackHandler.PRIORITY_ITEM_BIN;

public class StorageDrawersRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("storagedrawers", "drawer"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("drawer", "standard drawers", PRIORITY_ITEM_BIN);


	@Override
	public boolean canUseAsStorage(TileEntity te) {
		TileEntityDrawers drawer = (TileEntityDrawers) te;
		return drawer.isGroupValid() && CONFIG.isEnabled();
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
		return new StorageDrawerHandler((TileEntityDrawers) te);
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		StorageDrawerHandler handler = new StorageDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
