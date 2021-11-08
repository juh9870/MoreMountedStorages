package com.juh9870.pooptrain.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import eutros.framedcompactdrawers.block.tile.TileDrawersStandardCustom;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;

public class FramedCompactDrawersRegistry extends StorageDrawersRegistry {
	public static void register() {
		registerStorages(new FramedCompactDrawersRegistry(),
				TileDrawersStandardCustom.Slot1.class,
				TileDrawersStandardCustom.Slot2.class,
				TileDrawersStandardCustom.Slot4.class
		);
	}
}
