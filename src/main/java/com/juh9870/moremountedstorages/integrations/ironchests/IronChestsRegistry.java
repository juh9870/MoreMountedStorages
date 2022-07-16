package com.juh9870.moremountedstorages.integrations.ironchests;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.progwml6.ironchest.common.block.entity.IronChestsBlockEntityTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

public class IronChestsRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("ironchest", "chest"));
	public static final Config.RegistryInfo CONFIG = new Config.RegistryInfo("chest", "Iron Chests");


	@Override
	public boolean canUseAsStorage(BlockEntity te) {
		return super.canUseAsStorage(te) && CONFIG.isEnabled();
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public BlockEntityType<?>[] affectedStorages() {
		return new BlockEntityType[]{
				IronChestsBlockEntityTypes.IRON_CHEST.get(),
				IronChestsBlockEntityTypes.GOLD_CHEST.get(),
				IronChestsBlockEntityTypes.DIAMOND_CHEST.get(),
				IronChestsBlockEntityTypes.COPPER_CHEST.get(),
				// IronChestsBlockEntityTypes.SILVER_CHEST.get(),
				IronChestsBlockEntityTypes.CRYSTAL_CHEST.get(),
				IronChestsBlockEntityTypes.OBSIDIAN_CHEST.get(),
				IronChestsBlockEntityTypes.DIRT_CHEST.get(),
		};
	}
}
