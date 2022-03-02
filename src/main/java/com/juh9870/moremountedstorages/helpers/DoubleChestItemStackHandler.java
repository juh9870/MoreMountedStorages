package com.juh9870.moremountedstorages.helpers;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;

import java.util.Objects;

public abstract class DoubleChestItemStackHandler<T> extends SmartItemStackHandler {
    protected boolean second;

    public DoubleChestItemStackHandler() {
    }

    /**
     * This constructor assumes that double chest is given in {@code chest} parameter. Slots count of resulting handler is equal to exactly half of given handler
     *
     * @param chest  target {@link IItemHandler} of double chest
     * @param second specifies if this handler for second chest of a group, applying offset accordingly
     */
    protected DoubleChestItemStackHandler(IItemHandler chest, boolean second) {
        super(chest.getSlots() / 2);
        this.second = second;
        if (chest.getSlots() % 2 != 0) {
            throw new IllegalArgumentException("Given handler must have even number of slots");
        }
        int slots = getSlots();
        int offset = second ? slots : 0;
        copyItemsOver(chest, this, slots, offset, 0);
    }

    public void populateChest(IItemHandler chest) {
        boolean canClear = chest instanceof IItemHandlerModifiable;
        int offset = second ? chest.getSlots() / 2 : 0;
        // If target chest is single, offsets are ignored.
        if (chest.getSlots() == getSlots()) offset = 0;
        else if (chest.getSlots() != getSlots() * 2) {
            throw new IllegalArgumentException("Target handler isn't equal nor double the size of this handler");
        }
        copyItemsOver(this, chest, getSlots(), 0, offset);
    }

    protected abstract BlockPos secondHalfPos(BlockPos pos, BlockState state, T type);

    protected abstract void attachToOther(World level, BlockState self, BlockPos selfPos, BlockState other, BlockPos otherPos);

    protected abstract boolean isSingle(BlockState state);

    protected abstract T ownType();

    protected abstract T getType(BlockState state);

    protected abstract void setSingle(World level, BlockState state, BlockPos pos);

    protected boolean canConnect(BlockState self, BlockState other) {
        return Objects.equals(self.getBlock().getRegistryName(), other.getBlock().getRegistryName());
    }

    @Override
    public boolean addStorageToWorld(TileEntity te) {
        BlockState chestState = te.getBlockState();
        BlockPos neighbourPos = secondHalfPos(te.getBlockPos(), chestState, ownType());
        BlockState neighbourState = te.getLevel().getBlockState(neighbourPos);
        // If neighbour is the same id as this chest
        if (canConnect(chestState, neighbourState)) {
            boolean connect;
            // Connect if neighbour is single
            if (isSingle(neighbourState)) {
                connect = true;
            } else {
                // Or if neighbour is connected to us
                BlockPos neighbourConnection = secondHalfPos(
                        neighbourPos,
                        neighbourState,
                        getType(neighbourState));
                connect = neighbourConnection == te.getBlockPos();
            }
            if (connect) {
                IItemHandler otherHandler = te.getLevel().getBlockEntity(neighbourPos)
                        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                        .orElseThrow(() -> new RuntimeException("Neighbouring chest doesn't have inventory"));
//                IItemHandler tempHandler = new ItemStackHandler(otherHandler.getSlots());
//                copyItemsOver(otherHandler, tempHandler, tempHandler.getSlots(), 0, 0);
                attachToOther(te.getLevel(), chestState, te.getBlockPos(), neighbourState, neighbourPos);
                IItemHandler newOtherHandler = te.getLevel().getBlockEntity(neighbourPos)
                        .getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
                        .orElseThrow(() -> new RuntimeException("Updated neighbouring chest doesn't have inventory"));
                copyItemsOver(otherHandler, newOtherHandler, otherHandler.getSlots(), 0, second ? 0 : newOtherHandler.getSlots() / 2);
            } else {
                setSingle(te.getLevel(), chestState, te.getBlockPos());
            }
        } else {
            setSingle(te.getLevel(), chestState, te.getBlockPos());
        }
        te = te.getLevel().getBlockEntity(te.getBlockPos());
        LazyOptional<IItemHandler> handler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
        IItemHandler target = handler.orElseThrow(() -> new RuntimeException("Target tile entity doesn't have inventory"));
        populateChest(target);
        return false;
    }

    @Override
    public CompoundNBT serializeNBT() {
        CompoundNBT nbt = super.serializeNBT();
        nbt.putBoolean("second", second);
        return nbt;
    }

    @Override
    public void deserializeNBT(CompoundNBT nbt) {
        super.deserializeNBT(nbt);
        second = nbt.getBoolean("second");
    }
}
