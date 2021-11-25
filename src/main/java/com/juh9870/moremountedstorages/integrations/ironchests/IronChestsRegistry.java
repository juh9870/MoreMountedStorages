package com.juh9870.moremountedstorages.integrations.ironchests;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.Utils;
import com.progwml6.ironchest.common.block.tileentity.IronChestsTileEntityTypes;
import com.simibubi.create.api.contraption.ContraptionStorageRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class IronChestsRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("ironchest", "chest"));
	public static final Config.RegistryInfo CONFIG = new Config.RegistryInfo("chest", "Iron Chests");


	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && CONFIG.isEnabled();
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
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
