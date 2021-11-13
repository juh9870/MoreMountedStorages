package com.juh9870.pooptrain.integrations.immersiveengineering;

import blusunrize.immersiveengineering.common.IETileTypes;
import com.juh9870.pooptrain.Config;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ObjectHolder;

public class ImmersiveEngineeringRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance("immersiveengineering:crate");


	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && Config.IMMERSIVE_ENGINEERING.get();
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{IETileTypes.WOODEN_CRATE.get()};
	}
}
