package com.juh9870.pooptrain.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersComp;
import com.jaquadro.minecraft.storagedrawers.core.ModBlocks;
import com.juh9870.pooptrain.ContraptionItemStackHandler;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.registries.IForgeRegistry;

public class CompactingDrawerRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = createIfModLoaded(
			"storagedrawers",
			"storagedrawers:compacting_drawer",
			CompactingDrawerRegistry::new
	);

	public static void register(IForgeRegistry<ContraptionStorageRegistry> registry) {
		registry.register(INSTANCE.get());
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		TileEntityDrawersComp drawer = (TileEntityDrawersComp) te;
		return drawer.isGroupValid();
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModBlocks.Tile.FRACTIONAL_DRAWERS_3
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		return new CompactingDrawerHandler((TileEntityDrawersComp) te);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		StorageDrawerHandler handler = new CompactingDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
