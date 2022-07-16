package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import eutros.framedcompactdrawers.block.ModBlocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.common.util.Lazy;

public class FramedCompactingDrawerRegistry extends CompactingDrawerRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("framedcompactdrawers", "compacting_drawer"));

	@Override
	public BlockEntityType<?>[] affectedStorages() {
		return new BlockEntityType[]{
				ModBlocks.Tile.fractionalDrawers3
		};
	}
}
