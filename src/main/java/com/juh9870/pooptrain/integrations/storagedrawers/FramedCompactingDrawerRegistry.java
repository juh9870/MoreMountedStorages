package com.juh9870.pooptrain.integrations.storagedrawers;

import com.juh9870.pooptrain.ContraptionStorageRegistry;
import eutros.framedcompactdrawers.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;

public class FramedCompactingDrawerRegistry extends CompactingDrawerRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = createIfModLoaded(
			"framedcompactdrawers",
			"framedcompactdrawers:compacting_drawer",
			FramedCompactingDrawerRegistry::new
	);

	public static void register(IForgeRegistry<ContraptionStorageRegistry> registry) {
		registry.register(INSTANCE.get());
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.fractionalDrawers3
		};
	}
}
