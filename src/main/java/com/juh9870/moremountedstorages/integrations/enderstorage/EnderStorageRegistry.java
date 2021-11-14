package com.juh9870.moremountedstorages.integrations.enderstorage;

import codechicken.enderstorage.init.ModContent;
import codechicken.enderstorage.tile.TileEnderChest;
import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class EnderStorageRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("enderstorage", "ender_chest"));

	public static int managerGeneration = 0;

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && Config.ENDER_STORAGE.get();
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{ModContent.tileEnderChestType};
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		TileEnderChest chest = (TileEnderChest) te;
		return new EnderStackHandler(chest.getStorage());
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		EnderStackHandler handler = new EnderStackHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
