package com.juh9870.pooptrain.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersComp;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class CompactingDrawerRegistry extends ContraptionStorageRegistry {
	public static void register() {
		registerStorages(new CompactingDrawerRegistry(),
				TileEntityDrawersComp.class,

				TileEntityDrawersComp.Slot3.class
		);
		FramedCompactingDrawerRegistry.register();
	}

	@Override
	public boolean useCustomHandler() {
		return true;
	}

	@Override
	public boolean prepareStorageForContraption(TileEntity te) {
		TileEntityDrawersComp drawer = (TileEntityDrawersComp) te;
		return drawer.isGroupValid();
	}

	@Override
	public ItemStackHandler getHandler(TileEntity te) {
		return new CompactingDrawerHandler((TileEntityDrawersComp) te);
	}

	@Override
	public ItemStackHandler deserializeHandler(CompoundNBT nbt) {
		StorageDrawerHandler handler = new CompactingDrawerHandler();
		handler.deserializeNBT(nbt);
		return handler;
	}
}
