package com.juh9870.pooptrain.integrations.enderstorage;

import codechicken.enderstorage.init.ModContent;
import codechicken.enderstorage.tile.TileEnderChest;
import com.juh9870.pooptrain.ContraptionItemStackHandler;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;

public class EnderStorageRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE =
			createIfModLoaded(
					"enderstorage",
					"enderstorage:ender_chest",
					EnderStorageRegistry::new);

	public static int managerGeneration = 0;

	public static void register(IForgeRegistry<ContraptionStorageRegistry> registry) {
		registry.register(INSTANCE.get());
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
