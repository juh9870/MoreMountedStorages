package com.juh9870.moremountedstorages.integrations;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.DoubleChestItemStackHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.ChestBlock;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.state.properties.ChestType;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;

public class VanillaStorageRegistry extends ContraptionStorageRegistry {
    public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("minecraft", "storages"));
    public static final Config.RegistryInfo CONFIG = new Config.RegistryInfo("minecraft", "Vanilla Minecraft");

    @Override
    public Priority getPriority() {
        return Priority.NATIVE;
    }

    @Override
    public BlockEntityType<?>[] affectedStorages() {
        return new BlockEntityType[]{
                BlockEntityType.CHEST,
                BlockEntityType.TRAPPED_CHEST,
                BlockEntityType.BARREL,
                BlockEntityType.SHULKER_BOX,
        };
    }

    @Override
    public ContraptionItemStackHandler createHandler(BlockEntity te) {

        // Double chests use custom handler, other storages use default mounted logic
        if (te.getType() == BlockEntityType.CHEST || te.getType() == BlockEntityType.TRAPPED_CHEST) {
            ChestType type = te.getBlockState()
                    .getValue(ChestBlock.TYPE);
            if (type != ChestType.SINGLE) {
                return new McDoubleChestItemStackHandler(getHandlerFromDefaultCapability(te), type == ChestType.LEFT);
            }
        }

        return super.createHandler(te);
    }

    @Override
    public ContraptionItemStackHandler deserializeHandler(CompoundTag nbt) {
        return deserializeHandler(new McDoubleChestItemStackHandler(), nbt);
    }

    public static class McDoubleChestItemStackHandler extends DoubleChestItemStackHandler<ChestType> {
        public McDoubleChestItemStackHandler() {
        }

        public McDoubleChestItemStackHandler(IItemHandler chest, boolean second) {
            super(chest, second);
        }

        @Override
        public BlockPos secondHalfPos(BlockPos pos, BlockState state, ChestType type) {
            Direction facing = state.getValue(ChestBlock.FACING);
            Direction target;
            if (type == ChestType.LEFT) {
                target = facing.getClockWise();
            } else {
                target = facing.getCounterClockWise();
            }
            return pos.relative(target);
        }

        @Override
        public void attachToOther(Level level, BlockState self, BlockPos selfPos, BlockState other, BlockPos otherPos) {
            level.setBlockAndUpdate(
                    selfPos,
                    self.setValue(propertyName(), second ? ChestType.LEFT : ChestType.RIGHT)
            );
            level.setBlockAndUpdate(
                    otherPos,
                    other.setValue(propertyName(), second ? ChestType.RIGHT : ChestType.LEFT)
            );
            level.getBlockEntity(selfPos).setChanged();
            level.getBlockEntity(otherPos).setChanged();
        }

        @Override
        public boolean isSingle(BlockState state) {
            return state.getValue(propertyName()) == ChestType.SINGLE;
        }

        @Override
        protected ChestType ownType() {
            return second ? ChestType.LEFT : ChestType.RIGHT;
        }

        @Override
        protected ChestType getType(BlockState state) {
            return state.getValue(propertyName());
        }

        @Override
        protected void setSingle(Level level, BlockState state, BlockPos pos) {
            level.setBlockAndUpdate(
                    pos,
                    state.setValue(propertyName(), ChestType.SINGLE)
            );
        }

        @Override
        public int getPriority() {
            return 0;
        }

        @Override
        protected ContraptionStorageRegistry registry() {
            return INSTANCE.get();
        }

        protected EnumProperty<ChestType> propertyName(){
            return ChestBlock.TYPE;
        }
    }
}