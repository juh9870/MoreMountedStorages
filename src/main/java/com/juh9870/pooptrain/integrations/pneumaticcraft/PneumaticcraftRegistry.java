package com.juh9870.pooptrain.integrations.pneumaticcraft;

import com.juh9870.pooptrain.Config;
import com.juh9870.pooptrain.ContraptionItemStackHandler;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.juh9870.pooptrain.helpers.WrapperStackHandler;
import me.desht.pneumaticcraft.common.core.ModTileEntities;
import me.desht.pneumaticcraft.common.tileentity.TileEntitySmartChest;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ObjectHolder;

public class PneumaticcraftRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance("pneumaticcraft:smart_chest");


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
