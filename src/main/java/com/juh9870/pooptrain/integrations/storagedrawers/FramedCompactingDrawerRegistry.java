package com.juh9870.pooptrain.integrations.storagedrawers;

import eutros.framedcompactdrawers.block.tile.TileCompDrawersCustom;

public class FramedCompactingDrawerRegistry extends CompactingDrawerRegistry {
	public static void register() {
		registerStorages(new CompactingDrawerRegistry(),

				TileCompDrawersCustom.Slot3.class
		);
	}
}
