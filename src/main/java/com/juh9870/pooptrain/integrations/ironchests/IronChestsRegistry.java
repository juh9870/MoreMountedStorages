package com.juh9870.pooptrain.integrations.ironchests;

import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.progwml6.ironchest.common.block.tileentity.*;

public class IronChestsRegistry extends ContraptionStorageRegistry {
	public static void register() {
		registerStorages(new IronChestsRegistry(),
				IronChestTileEntity.class,
				GoldChestTileEntity.class,
				DiamondChestTileEntity.class,
				CopperChestTileEntity.class,
				SilverChestTileEntity.class,
				CrystalChestTileEntity.class,
				ObsidianChestTileEntity.class,
				DirtChestTileEntity.class
		);
	}

	@Override
	public boolean useCustomHandler() {
		return false;
	}
}
