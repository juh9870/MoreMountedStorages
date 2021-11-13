package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import eutros.framedcompactdrawers.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.ObjectHolder;

public class FramedCompactingDrawerRegistry extends CompactingDrawerRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance("framedcompactdrawers:compacting_drawer");

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.fractionalDrawers3
		};
	}
}
