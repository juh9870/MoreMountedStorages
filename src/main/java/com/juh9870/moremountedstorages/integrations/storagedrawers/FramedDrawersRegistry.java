package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import eutros.framedcompactdrawers.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class FramedDrawersRegistry extends StorageDrawersRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("framedcompactdrawers", "drawer"));

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.standardDrawers1,
				ModBlocks.Tile.standardDrawers2,
				ModBlocks.Tile.standardDrawers4,
		};
	}
}
