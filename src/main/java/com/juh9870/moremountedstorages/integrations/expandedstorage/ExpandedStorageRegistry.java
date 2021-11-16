package com.juh9870.moremountedstorages.integrations.expandedstorage;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.CursedChestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpandedStorageRegistry extends ContraptionStorageRegistry {

	public static Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("expandedstorage", "chest"));
	public static final Config.RegistryInfo CONFIG = new Config.RegistryInfo("chest", "Expanded Storage");
	private static final Lazy<TileEntityType<?>[]> affectedStorages = Lazy.of(() -> {
		List<TileEntityType<?>> values = new ArrayList<>();
		for (TileEntityType<?> tileEntity : ForgeRegistries.TILE_ENTITIES) {
			if (tileEntity.getRegistryName().getNamespace().equals("expandedstorage")) {
				values.add(tileEntity);
			}
		}

		return values.toArray(new TileEntityType<?>[0]);
	});

	@SuppressWarnings("UnstableApiUsage")
	@Override
	public boolean canUseAsStorage(TileEntity te) {
		if (!super.canUseAsStorage(te) && CONFIG.isEnabled()) return false;
		Optional<CursedChestType> type = te.getBlockState().getOptionalValue(AbstractChestBlock.CURSED_CHEST_TYPE);
		return !type.isPresent() || (type.get() != CursedChestType.SINGLE &&
				type.get() != CursedChestType.TOP &&
				type.get() != CursedChestType.FRONT &&
				type.get() != CursedChestType.LEFT);
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return affectedStorages.get();
	}
}
