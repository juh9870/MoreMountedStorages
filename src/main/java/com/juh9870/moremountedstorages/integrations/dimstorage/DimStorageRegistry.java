package com.juh9870.moremountedstorages.integrations.dimstorage;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import edivad.dimstorage.setup.Registration;
import edivad.dimstorage.blockentities.BlockEntityDimChest;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

public class DimStorageRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("dimstorage", "dimensional_chest"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("dimensional_chest", "Dimensional Chest", 1);
	public static int managerGeneration = 0;

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
		return new BlockEntityType[]{Registration.DIMCHEST_TILE.get()};
	}

	@Override
	public ContraptionItemStackHandler createHandler(BlockEntity te) {
		BlockEntityDimChest chest = (BlockEntityDimChest) te;
		return new DimStorageStackHandler(chest.getStorage());
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
		DimStorageStackHandler handler = new DimStorageStackHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
