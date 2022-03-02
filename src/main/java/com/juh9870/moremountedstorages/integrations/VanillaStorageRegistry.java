package com.juh9870.moremountedstorages.integrations;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.DoubleChestItemStackHandler;
import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.state.properties.ChestType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;

import java.util.Objects;

public class VanillaStorageRegistry extends ContraptionStorageRegistry {
    public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("minecraft", "storages"));
    public static final Config.RegistryInfo CONFIG = new Config.RegistryInfo("minecraft", "Vanilla Minecraft");

    @Override
    public Priority getPriority() {
        return Priority.NATIVE;
    }

    @Override
    public TileEntityType<?>[] affectedStorages() {
        return new TileEntityType[]{
                TileEntityType.CHEST,
                TileEntityType.TRAPPED_CHEST,
                TileEntityType.BARREL,
                TileEntityType.SHULKER_BOX,
        };
    }

    @Override
    public ContraptionItemStackHandler createHandler(TileEntity te) {

        // Double chests use custom handler, other storages use default mounted logic
        if (te.getType() == TileEntityType.CHEST || te.getType() == TileEntityType.TRAPPED_CHEST) {
            ChestType type = te.getBlockState()
                    .getValue(ChestBlock.TYPE);
            if (type != ChestType.SINGLE) {
                return new VanillaDoubleChestItemStackHandler(getHandlerFromDefaultCapability(te), type == ChestType.LEFT);
            }
        }

        return super.createHandler(te);
    }

    @Override
    public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
        return deserializeHandler(new VanillaDoubleChestItemStackHandler(), nbt);
    }

    public static class VanillaDoubleChestItemStackHandler extends DoubleChestItemStackHandler<ChestType> {
        public VanillaDoubleChestItemStackHandler() {
        }

        public VanillaDoubleChestItemStackHandler(IItemHandler chest, boolean second) {
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
        public void attachToOther(World level, BlockState self, BlockPos selfPos, BlockState other, BlockPos otherPos) {
//            ((ChestBlock)self.getBlock()).combine(other, level, otherPos, true);
            level.setBlockAndUpdate(
                    selfPos,
                    self.setValue(ChestBlock.TYPE, second ? ChestType.LEFT : ChestType.RIGHT)
            );
//            level.setBlockAndUpdate(
//                    otherPos,
//                    other.setValue(ChestBlock.TYPE, second ? ChestType.RIGHT : ChestType.LEFT)
//            );
            level.getBlockEntity(selfPos).clearCache();
            level.getBlockEntity(otherPos).clearCache();
        }

        @Override
        public boolean isSingle(BlockState state) {
            return state.getValue(ChestBlock.TYPE) == ChestType.SINGLE;
        }

        @Override
        protected ChestType ownType() {
            return second ? ChestType.LEFT : ChestType.RIGHT;
        }

        @Override
        protected ChestType getType(BlockState state) {
            return state.getValue(ChestBlock.TYPE);
        }

        @Override
        protected void setSingle(World level, BlockState state, BlockPos pos) {
            level.setBlockAndUpdate(
                    pos,
                    state.setValue(ChestBlock.TYPE, ChestType.SINGLE)
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
    }
}