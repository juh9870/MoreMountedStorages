package com.juh9870.pooptrain.integrations.immersiveengineering;

import blusunrize.immersiveengineering.common.blocks.wooden.WoodenCrateTileEntity;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.progwml6.ironchest.common.block.tileentity.*;

public class ImmersiveEngineeringRegistry extends ContraptionStorageRegistry {
	public static void register() {
		registerStorages(new ImmersiveEngineeringRegistry(),
				WoodenCrateTileEntity.class
		);
	}

	@Override
	public boolean useCustomHandler() {
		return false;
	}
}
