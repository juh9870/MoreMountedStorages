package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.rydelfox.morestoragedrawers.block.tile.Tiles;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class MoreDrawersRegistry extends StorageDrawersRegistry {
    public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("morestoragedrawers", "drawer"));

    @Override
    public TileEntityType<?>[] affectedStorages() {
        return new TileEntityType[]{
                Tiles.TILE_DRAWERS_1,
                Tiles.TILE_DRAWERS_2,
                Tiles.TILE_DRAWERS_4,
        };
    }
}
