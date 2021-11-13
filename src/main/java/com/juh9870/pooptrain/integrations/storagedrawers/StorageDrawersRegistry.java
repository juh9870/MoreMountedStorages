package com.juh9870.pooptrain.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import com.juh9870.pooptrain.ContraptionItemStackHandler;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;

public class StorageDrawersRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = createIfModLoaded(
			"storagedrawers",
			"storagedrawers:drawer",
			StorageDrawersRegistry::new
	);

	public static void register(IForgeRegistry<ContraptionStorageRegistry> registry) {
		registry.register(INSTANCE.get());
		CompactingDrawerRegistry.register(registry);
		FramedDrawersRegistry.register(registry);
	}

	public static ForgeConfigSpec.ConfigValue<Boolean> enabled;

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		TileEntityDrawersStandard drawer = (TileEntityDrawersStandard) te;
		return drawer.isGroupValid() && enabled.get();
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.STANDARD_DRAWERS_1,
				ModBlocks.Tile.STANDARD_DRAWERS_2,
				ModBlocks.Tile.STANDARD_DRAWERS_4,
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		return new StorageDrawerHandler((TileEntityDrawersStandard) te);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		StorageDrawerHandler handler = new StorageDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
