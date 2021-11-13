package com.juh9870.pooptrain.integrations.ironchests;

import com.juh9870.pooptrain.Config;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class IronChestsRegistry extends ContraptionStorageRegistry {
	//@ObjectHolder("")
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance("ironchest:chest");


	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && Config.IRON_CHESTS.get();
	}


	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				IronChestsTileEntityTypes.IRON_CHEST.get(),
				IronChestsTileEntityTypes.GOLD_CHEST.get(),
				IronChestsTileEntityTypes.DIAMOND_CHEST.get(),
				IronChestsTileEntityTypes.COPPER_CHEST.get(),
				IronChestsTileEntityTypes.SILVER_CHEST.get(),
				IronChestsTileEntityTypes.CRYSTAL_CHEST.get(),
				IronChestsTileEntityTypes.OBSIDIAN_CHEST.get(),
				IronChestsTileEntityTypes.DIRT_CHEST.get(),
		};
	}
}
