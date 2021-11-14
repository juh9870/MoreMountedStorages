package com.juh9870.moremountedstorages.integrations.pneumaticcraft;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.WrapperStackHandler;
import me.desht.pneumaticcraft.common.core.ModTileEntities;
import me.desht.pneumaticcraft.common.tileentity.TileEntitySmartChest;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class PneumaticcraftRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("pneumaticcraft", "smart_chest"));


	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModTileEntities.SMART_CHEST.get()
		};
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return getHandlerFromDefaultCapability(te) instanceof TileEntitySmartChest.SmartChestItemHandler && Config.PNEUMATICCRAFT.get();
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {

		IItemHandler handler = getHandlerFromDefaultCapability(te);
		if (handler == dummyHandler) {
			return null;
		}
		if (!(handler instanceof TileEntitySmartChest.SmartChestItemHandler)) {
			return null;
		}

		return new SmartChestWrapper((TileEntitySmartChest.SmartChestItemHandler) handler);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return new SmartChestWrapper((ItemStackHandler) TileEntitySmartChest.deserializeSmartChest(nbt));
	}

	public static class SmartChestWrapper extends WrapperStackHandler {

		public SmartChestWrapper(ItemStackHandler handler) {
			super(handler);
		}

		@Override
		protected ContraptionStorageRegistry registry() {
			return INSTANCE.get();
		}
	}
}
