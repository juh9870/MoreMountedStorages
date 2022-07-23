// not supported in 1.18.2 yet
//package com.juh9870.moremountedstorages.integrations.storageoverhaul;
//
//import com.juh9870.moremountedstorages.Config;
//import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
//import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
//import com.juh9870.moremountedstorages.Utils;
//import com.juh9870.moremountedstorages.helpers.AdvancedItemStackHandler;
//import de.maxhenkel.storage.blocks.BlockEntity.ModTileEntities;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraftforge.common.util.Lazy;
//import net.minecraftforge.items.IItemHandler;
//
//import static com.juh9870.moremountedstorages.ContraptionItemStackHandler.PRIORITY_ITEM_BIN;
//
//public class StorageBarrelRegistry extends ContraptionStorageRegistry {
//    public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("storage_overhaul", "barrel"));
//    public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("barrel", "Storage Overhaul Storage Barrel", PRIORITY_ITEM_BIN);
//
//    @Override
//    public boolean canUseAsStorage(BlockEntity te) {
//        return super.canUseAsStorage(te) && CONFIG.isEnabled();
//    }
//
//    @Override
//    public Priority getPriority() {
//        return Priority.ADDON;
//    }
//
//    @Override
//    public BlockEntityType<?>[] affectedStorages() {
//        return new BlockEntityType[]{
//                ModTileEntities.STORAGE_BARREL
//        };
//    }
//
//    @Override
//    public ContraptionItemStackHandler createHandler(BlockEntity te) {
//        return new BarrelItemStackHandler(getHandlerFromDefaultCapability(te));
//    }
//
//    @Override
//    public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
//        return deserializeHandler(new BarrelItemStackHandler(), nbt);
//    }
//
//    public static class BarrelItemStackHandler extends AdvancedItemStackHandler {
//
//        public BarrelItemStackHandler() {
//        }
//
//        public BarrelItemStackHandler(IItemHandler handler) {
//            super(handler);
//        }
//
//        public BarrelItemStackHandler(int size, int stackSize) {
//            super(size, stackSize);
//        }
//
//        @Override
//        public boolean addStorageToWorld(BlockEntity te) {
//            simpleOverwrite(getHandlerFromDefaultCapability(te));
//            return false;
//        }
//
//        @Override
//        public int getPriority() {
//            return CONFIG.getPriority();
//        }
//
//        @Override
//        protected ContraptionStorageRegistry registry() {
//            return INSTANCE.get();
//        }
//    }
//}
