package com.juh9870.pooptrain.integrations.storagedrawers;

import eutros.framedcompactdrawers.block.tile.TileDrawersStandardCustom;

public class FramedCompactDrawersRegistry extends StorageDrawersRegistry {
	public static void register() {
		registerStorages(new FramedCompactDrawersRegistry(),
				TileDrawersStandardCustom.Slot1.class,
				TileDrawersStandardCustom.Slot2.class,
				TileDrawersStandardCustom.Slot4.class
		);
	}
}
