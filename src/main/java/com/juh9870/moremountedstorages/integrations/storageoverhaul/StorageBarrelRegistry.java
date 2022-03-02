package com.juh9870.moremountedstorages.integrations.storageoverhaul;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.AdvancedItemStackHandler;
import de.maxhenkel.storage.blocks.tileentity.ModTileEntities;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;

import static com.juh9870.moremountedstorages.ContraptionItemStackHandler.PRIORITY_ITEM_BIN;

public class StorageBarrelRegistry extends ContraptionStorageRegistry {
    public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("storage_overhaul", "barrel"));
    public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("barrel", "Storage Overhaul Storage Barrel", PRIORITY_ITEM_BIN);

    @Override
    public boolean canUseAsStorage(TileEntity te) {
        return super.canUseAsStorage(te) && CONFIG.isEnabled();
    }

    @Override
    public Priority getPriority() {
        return Priority.ADDON;
    }

    @Override
    public TileEntityType<?>[] affectedStorages() {
        return new TileEntityType[]{
                ModTileEntities.STORAGE_BARREL
        };
    }

    @Override
    public ContraptionItemStackHandler createHandler(TileEntity te) {
        return new BarrelItemStackHandler(getHandlerFromDefaultCapability(te));
    }

    @Override
    public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
        return deserializeHandler(new BarrelItemStackHandler(), nbt);
    }

    public static class BarrelItemStackHandler extends AdvancedItemStackHandler {

        public BarrelItemStackHandler() {
        }

        public BarrelItemStackHandler(IItemHandler handler) {
            super(handler);
        }

        public BarrelItemStackHandler(int size, int stackSize) {
            super(size, stackSize);
        }

        @Override
        public boolean addStorageToWorld(TileEntity te) {
            simpleOverwrite(getHandlerFromDefaultCapability(te));
            return false;
        }

        @Override
        public int getPriority() {
            return CONFIG.getPriority();
        }

        @Override
        protected ContraptionStorageRegistry registry() {
            return INSTANCE.get();
        }
    }
}
