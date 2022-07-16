package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersComp;
import com.jaquadro.minecraft.storagedrawers.core.ModBlockEntities;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

import static com.juh9870.moremountedstorages.ContraptionItemStackHandler.PRIORITY_ITEM_BIN;

public class CompactingDrawerRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("storagedrawers", "compacting_drawer"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("compacting_drawer", "compacting drawers", PRIORITY_ITEM_BIN);

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public boolean canUseAsStorage(BlockEntity te) {
		TileEntityDrawersComp drawer = (TileEntityDrawersComp) te;
		return drawer.isGroupValid() && CONFIG.isEnabled();
	}

	@Override
	public BlockEntityType<?>[] affectedStorages() {
		return new BlockEntityType[]{
				ModBlockEntities.FRACTIONAL_DRAWERS_3.get()
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(BlockEntity te) {
		return new CompactingDrawerHandler((TileEntityDrawersComp) te);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
		StorageDrawerHandler handler = new CompactingDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
