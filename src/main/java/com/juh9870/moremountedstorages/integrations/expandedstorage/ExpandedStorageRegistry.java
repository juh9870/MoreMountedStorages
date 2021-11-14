package com.juh9870.moremountedstorages.integrations.expandedstorage;

import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import net.minecraft.block.ChestBlock;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpandedStorageRegistry extends ContraptionStorageRegistry {

	private static final Lazy<TileEntityType<?>[]> affectedStorages = Lazy.of(() -> {
		List<TileEntityType<?>> values = new ArrayList<>();
		for (TileEntityType<?> tileEntity : ForgeRegistries.TILE_ENTITIES) {
			if (tileEntity.getRegistryName().getNamespace().equals("expandedstorage")) {
				values.add(tileEntity);
			}
		}

		return values.toArray(new TileEntityType<?>[0]);
	});
	public static Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("expandedstorage", "chest"));

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		Optional<ChestType> type = te.getBlockState().getOptionalValue(ChestBlock.TYPE);
		if (type.isPresent() && type.get() != ChestType.SINGLE)
			te.getLevel().setBlockAndUpdate(te.getBlockPos(),
							te.getBlockState().setValue(ChestBlock.TYPE, ChestType.SINGLE));
		te.clearCache();
		return null;
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return affectedStorages.get();
	}
}
