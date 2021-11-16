package com.juh9870.moremountedstorages.integrations.immersiveengineering;

import blusunrize.immersiveengineering.common.IETileTypes;
import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class ImmersiveEngineeringRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("immersiveengineering", "crate"));

	public static final Config.RegistryInfo CONFIG = new Config.RegistryInfo("crate", "Immersive Engineering Crate");

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && CONFIG.isEnabled();
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{IETileTypes.WOODEN_CRATE.get()};
	}
}
