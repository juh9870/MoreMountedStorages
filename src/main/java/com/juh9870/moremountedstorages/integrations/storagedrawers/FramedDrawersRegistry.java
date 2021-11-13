package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import eutros.framedcompactdrawers.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ObjectHolder;

public class FramedDrawersRegistry extends StorageDrawersRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance("framedcompactdrawers:drawer");

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.standardDrawers1,
				ModBlocks.Tile.standardDrawers2,
				ModBlocks.Tile.standardDrawers4,
		};
	}
}
