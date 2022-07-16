package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersComp;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.FractionalDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.UpgradeData;
import com.jaquadro.minecraft.storagedrawers.config.CommonConfig;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;

public class CompactingDrawerHandler extends StorageDrawerHandler {

	public CompactingDrawerHandler() {
		super();
	}

	public CompactingDrawerHandler(UpgradeData upgrades, IDrawerAttributes attributes, @Nullable IDrawerGroup group, int units, Level world) {
		super(upgrades, attributes, group, units, world);
	}

	public CompactingDrawerHandler(TileEntityDrawers drawer) {
		super(drawer);
	}

	@Override
	public void copyItems(IDrawerGroup from, IDrawerGroup to) {
		if (!(from instanceof FractionalDrawerGroup))
			throw new IllegalArgumentException("Invalid source drawer type: expected FractionalDrawerGroup, got " + from.getClass());
		if (!(to instanceof FractionalDrawerGroup))
			throw new IllegalArgumentException("Invalid target drawer type: expected FractionalDrawerGroup, got " + to.getClass());
		if (from.getDrawerCount() != to.getDrawerCount())
			throw new IllegalArgumentException("Provided drawers dimensions mismatch");
		to.getDrawer(0).setStoredItem(from.getDrawer(0).getStoredItemPrototype());
		((FractionalDrawerGroup) to).setPooledCount(((FractionalDrawerGroup) from).getPooledCount());
	}

	@Override
	protected IDrawerGroup createGroup(int drawers) {
		return new CompactingDrawerGroup(drawers);
	}

	@Override
	public boolean addStorageToWorld(BlockEntity te) {
		clearGroup((TileEntityDrawersComp) te);
		copyItemsTo((TileEntityDrawersComp) te);
		copyItemsTo((TileEntityDrawersComp) te.getLevel().getBlockEntity(te.getBlockPos()));
		return false;
	}

	@Override
	protected ContraptionStorageRegistry registry() {
		return CompactingDrawerRegistry.INSTANCE.get();
	}

	@Override
	public int getPriority() {
		return CompactingDrawerRegistry.CONFIG.getPriority();
	}

	public class CompactingDrawerGroup extends FractionalDrawerGroup {

		public CompactingDrawerGroup(int slotCount) {
			super(slotCount);
			setCapabilityProvider(CompactingDrawerHandler.this);
		}

		@Override
		protected int getStackCapacity() {
			return upgradeData.getStorageMultiplier() *
					(upgradeData.hasOneStackUpgrade() ? 1 :
							storageUnits * CommonConfig.GENERAL.baseStackStorage.get());
		}

		@Override
		protected Level getWorld() {
			return world;
		}
	}
}
