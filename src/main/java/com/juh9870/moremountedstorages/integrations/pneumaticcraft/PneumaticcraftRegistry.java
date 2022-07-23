package com.juh9870.moremountedstorages.integrations.pneumaticcraft;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.WrapperStackHandler;
import me.desht.pneumaticcraft.common.core.ModBlockEntities;
import me.desht.pneumaticcraft.common.block.entity.SmartChestBlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class PneumaticcraftRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("pneumaticcraft", "smart_chest"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("smart_chest", "PneumaticCraft Smart Chest", 1);


	@Override
	public BlockEntityType<?>[] affectedStorages() {
		return new BlockEntityType[]{
                ModBlockEntities.SMART_CHEST.get()
		};
	}

	@Override
	public boolean canUseAsStorage(BlockEntity te) {
		return getHandlerFromDefaultCapability(te) instanceof SmartChestBlockEntity.SmartChestItemHandler && CONFIG.isEnabled();
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public ContraptionItemStackHandler createHandler(BlockEntity te) {

		IItemHandler handler = getHandlerFromDefaultCapability(te);
		if (handler == dummyHandler) {
			return null;
		}
		if (!(handler instanceof SmartChestBlockEntity.SmartChestItemHandler)) {
			return null;
		}

		return new SmartChestWrapper((SmartChestBlockEntity.SmartChestItemHandler) handler);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
		return new SmartChestWrapper((ItemStackHandler) SmartChestBlockEntity.deserializeSmartChest(nbt));
	}

	public static class SmartChestWrapper extends WrapperStackHandler {

		public SmartChestWrapper(ItemStackHandler handler) {
			super(handler);
		}

		@Override
		protected ContraptionStorageRegistry registry() {
			return INSTANCE.get();
		}

		@Override
		public int getPriority() {
			return CONFIG.getPriority();
		}
	}
}
