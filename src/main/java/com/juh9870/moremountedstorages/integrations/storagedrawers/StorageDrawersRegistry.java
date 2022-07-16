package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.core.ModBlockEntities;
import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

import static com.juh9870.moremountedstorages.ContraptionItemStackHandler.PRIORITY_ITEM_BIN;

public class StorageDrawersRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("storagedrawers", "drawer"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("drawer", "standard drawers", PRIORITY_ITEM_BIN);


	@Override
	public boolean canUseAsStorage(BlockEntity te) {
		TileEntityDrawersStandard drawer = (TileEntityDrawersStandard) te;
		return drawer.isGroupValid() && CONFIG.isEnabled();
	}

	@Override
	public BlockEntityType<?>[] affectedStorages() {
		return new BlockEntityType[]{
				ModBlockEntities.STANDARD_DRAWERS_1.get(),
				ModBlockEntities.STANDARD_DRAWERS_2.get(),
				ModBlockEntities.STANDARD_DRAWERS_4.get(),
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(BlockEntity te) {
		return new StorageDrawerHandler((TileEntityDrawersStandard) te);
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
		StorageDrawerHandler handler = new StorageDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
