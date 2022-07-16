package com.juh9870.moremountedstorages.integrations.enderchests;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;
import shetiphian.enderchests.Values;
import net.minecraft.world.level.block.entity.EnderChestBlockEntity;
import shetiphian.enderchests.common.block.BlockEnderChest;
import shetiphian.enderchests.common.tileentity.TileEntityEnderChest;

public class EnderChestsRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("enderchests", "ender_chest"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("ender_chest", "Ender Chests", 1);
	public static int managerGeneration = 0;


	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public boolean canUseAsStorage(BlockEntity te) {
		return super.canUseAsStorage(te) && CONFIG.isEnabled() && getHandlerFromDefaultCapability(te) != dummyHandler;
	}

	@Override
	public BlockEntityType<?>[] affectedStorages() {
		return new BlockEntityType[]{
				Values.tileEnderChest
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(BlockEntity te) {
		return new EnderChestHandler((TileEntityEnderChest) te);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
		return deserializeHandler(new EnderChestHandler(), nbt);
	}
}
