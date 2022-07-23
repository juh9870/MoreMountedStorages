// not supported in 1.18.2 yet
//package com.juh9870.moremountedstorages.integrations.storageoverhaul;
//
//import com.juh9870.moremountedstorages.Config;
//import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
//import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
//import com.juh9870.moremountedstorages.Utils;
//import com.juh9870.moremountedstorages.helpers.SmartItemStackHandler;
//import com.juh9870.moremountedstorages.integrations.VanillaStorageRegistry;
//import de.maxhenkel.storage.blocks.ModChestBlock;
//import de.maxhenkel.storage.blocks.BlockEntity.ModTileEntities;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.state.EnumProperty;
//import net.minecraft.state.properties.ChestType;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.BlockEntity.BlockEntityMerger;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.core.NonNullList;
//import net.minecraft.util.math.BlockPos;
//import net.minecraftforge.common.util.Lazy;
//import net.minecraftforge.items.IItemHandler;
//
//import java.lang.ref.WeakReference;
//import java.util.WeakHashMap;
//
//public class StorageOverhaulRegistry extends ContraptionStorageRegistry {
//    public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("storage_overhaul", "storages"));
//    public static final Config.RegistryInfo CONFIG = new Config.RegistryInfo("storages", "Storage Overhaul Chest, Barrel and Shulker box");
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
//                ModTileEntities.CHEST,
//                ModTileEntities.BARREL,
//                ModTileEntities.SHULKER_BOX,
//        };
//    }
//
//    @Override
//    public ContraptionItemStackHandler createHandler(BlockEntity te) {
//
//        // Try to split double chests
//        if (te.getType() == ModTileEntities.CHEST) {
//            if (ModChestBlock.getType(te.getBlockState()) != BlockEntityMerger.Type.SINGLE) {
//                te.getLevel().setBlockAndUpdate(
//                        te.getBlockPos(),
//                        te.getBlockState().setValue(ModChestBlock.TYPE, ChestType.SINGLE)
//                );
//                te.clearCache();
//            }
//            return new ChestSplittingHandler(getHandlerFromDefaultCapability(te));
//        }
//
//        return super.createHandler(te);
//    }
//
//    @Override
//    public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
//        return deserializeHandler(new ChestSplittingHandler(), nbt);
//    }
//
//    public static class ChestSplittingHandler extends SmartItemStackHandler {
//
//
//        /**
//         * FIXME:
//         * Not the best solution, but I don't understand why items get cleared away from the chest upon block update.
//         * Using WeakHashMap, so keys and values are garbage collected once instance of this handler is gone from
//         * memory, aka when contraption is fully disassembled.
//         */
//        private static final WeakHashMap<BlockPos, IItemHandler> tempHandlers = new WeakHashMap<>();
//
//        /**
//         * Used to hold BlockPos while this object is referenced to keep value in {@link ChestSplittingHandler#tempHandlers}
//         */
//        private BlockPos deployPosition;
//
//        public ChestSplittingHandler() {
//        }
//
//        public ChestSplittingHandler(IItemHandler handler) {
//            super(handler);
//        }
//
//        @Override
//        public int getPriority() {
//            return 0;
//        }
//
//        @Override
//        public boolean addStorageToWorld(BlockEntity te) {
//            deployPosition = te.getBlockPos();
//
//            IItemHandler target = getHandlerFromDefaultCapability(te);
//            BlockEntityMerger.Type type = ModChestBlock.getType(te.getBlockState());
//
//            // Remove old key in case map had old value that wasn't garbage collected yet
//            // Put doesn't replace key if it already exists, so need removal to ensure that key is tied to living instance
//            tempHandlers.remove(deployPosition);
//            tempHandlers.put(deployPosition, simpleCopy());
//
//            // Check if paired chest position is cached, and if yes, fill target with it too,
//            // because it's getting voided by default
//            if (type != BlockEntityMerger.Type.SINGLE) {
//                BlockPos otherPos = deployPosition.relative(ModChestBlock.getDirectionToAttached(te.getBlockState()));
//                IItemHandler otherHandler = tempHandlers.get(otherPos);
//                if (otherHandler != null && target.getSlots() == getSlots() * 2) {
//                    int offset = 0;
//                    if (type == BlockEntityMerger.Type.FIRST) {
//                        offset = target.getSlots() / 2;
//                    }
//                    copyItemsOver(otherHandler, target, otherHandler.getSlots(), 0, offset);
//                }
//            }
//
//            // Fill target from this handler normally, even tho it may get voided later
//            int offset = 0;
//            if (type == BlockEntityMerger.Type.SECOND && target.getSlots() == getSlots() * 2) {
//                offset = target.getSlots() / 2;
//            }
//            copyItemsOver(this, target, getSlots(), 0, offset);
//            return false;
//        }
//
//        @Override
//        protected ContraptionStorageRegistry registry() {
//            return INSTANCE.get();
//        }
//    }
//}
