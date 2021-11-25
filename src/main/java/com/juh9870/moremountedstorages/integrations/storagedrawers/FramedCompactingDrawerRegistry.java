package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.juh9870.moremountedstorages.Utils;
import com.simibubi.create.api.contraption.ContraptionStorageRegistry;
import eutros.framedcompactdrawers.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class FramedCompactingDrawerRegistry extends CompactingDrawerRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("framedcompactdrawers", "compacting_drawer"));

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.fractionalDrawers3
		};
	}
}
