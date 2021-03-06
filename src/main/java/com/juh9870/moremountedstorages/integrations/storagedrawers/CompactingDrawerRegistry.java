package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersComp;
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

public class CompactingDrawerRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("storagedrawers", "compacting_drawer"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("compacting_drawer", "compacting drawers", PRIORITY_ITEM_BIN);

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		TileEntityDrawersComp drawer = (TileEntityDrawersComp) te;
		return drawer.isGroupValid() && CONFIG.isEnabled();
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.FRACTIONAL_DRAWERS_3
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		return new CompactingDrawerHandler((TileEntityDrawersComp) te);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		StorageDrawerHandler handler = new CompactingDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
