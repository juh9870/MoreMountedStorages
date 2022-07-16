package com.juh9870.moremountedstorages.integrations.enderstorage;

import codechicken.enderstorage.init.EnderStorageModContent;
import codechicken.enderstorage.tile.TileEnderChest;
import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

public class EnderStorageRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("enderstorage", "ender_chest"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("ender_chest", "Ender Storage", 1);
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
		return new BlockEntityType[]{EnderStorageModContent.ENDER_CHEST_TILE.get()};
	}

	@Override
	public ContraptionItemStackHandler createHandler(BlockEntity te) {
		TileEnderChest chest = (TileEnderChest) te;
		return new EnderStackHandler(chest.getStorage());
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
		EnderStackHandler handler = new EnderStackHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
