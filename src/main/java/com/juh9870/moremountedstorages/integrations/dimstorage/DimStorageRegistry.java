package com.juh9870.moremountedstorages.integrations.dimstorage;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.Utils;
import com.simibubi.create.api.contraption.ContraptionItemStackHandler;
import com.simibubi.create.api.contraption.ContraptionStorageRegistry;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.tile.TileEntityDimChest;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class DimStorageRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("dimstorage", "dimensional_chest"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("dimensional_chest", "Dimensional Chest", 1);
	public static int managerGeneration = 0;

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
		return new TileEntityType[]{Registration.DIMCHEST_TILE.get()};
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		TileEntityDimChest chest = (TileEntityDimChest) te;
		return new DimStorageStackHandler(chest.getStorage());
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		DimStorageStackHandler handler = new DimStorageStackHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
