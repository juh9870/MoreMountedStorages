package com.juh9870.pooptrain.integrations.storagedrawers;

import com.juh9870.pooptrain.ContraptionStorageRegistry;
import eutros.framedcompactdrawers.block.ModBlocks;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;

public class FramedDrawersRegistry extends StorageDrawersRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = createIfModLoaded(
			"framedcompactdrawers",
			"framedcompactdrawers:drawer",
			FramedDrawersRegistry::new
	);

	public static void register(IForgeRegistry<ContraptionStorageRegistry> registry) {
		registry.register(INSTANCE.get());
		FramedCompactingDrawerRegistry.register(registry);
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.standardDrawers1,
				ModBlocks.Tile.standardDrawers2,
				ModBlocks.Tile.standardDrawers4,
		};
	}
}
