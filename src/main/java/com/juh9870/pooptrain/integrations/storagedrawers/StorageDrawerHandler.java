package com.juh9870.pooptrain.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributesModifiable;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.UpgradeData;
import com.jaquadro.minecraft.storagedrawers.capabilities.BasicDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.config.CommonConfig;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StorageDrawerHandler extends ItemStackHandler implements ICapabilityProvider {
	@CapabilityInject(IDrawerAttributes.class)
	public static Capability<IDrawerAttributes> DRAWER_ATTRIBUTES_CAPABILITY = null;
	private final UpgradeData upgradeData;
	private final BasicDrawerAttributes drawerAttributes;
	private final LazyOptional<?> attributesHandler = LazyOptional.of(this::getDrawerAttributes);
	private StandardDrawerGroup drawerGroup;
	private int storageUnits;
	private int drawers;

	public StorageDrawerHandler() {
		this(new UpgradeData(7), new BasicDrawerAttributes(), null, 1);
	}

	public StorageDrawerHandler(UpgradeData upgrades, IDrawerAttributes attributes, @Nullable IDrawerGroup group, int units) {
		super();
		this.storageUnits = units;

		upgradeData = new UpgradeData(upgrades.getSlotCount());
		copyUpgrades(upgrades, upgradeData);

		drawerAttributes = new BasicDrawerAttributes();
		copyAttributes(attributes, drawerAttributes);

		drawers = group == null ? 4 : group.getDrawerCount();
		drawerGroup = new ContraptionDrawerGroup(drawers);
		copyItems(group, drawerGroup);

	}

	public StorageDrawerHandler(TileEntityDrawersStandard drawer) {
		this(drawer.upgrades(), drawer.getDrawerAttributes(), drawer.getGroup(), drawer.getDrawerCapacity());
	}

	private static void copyUpgrades(@Nullable UpgradeData from, UpgradeData to) {
		if (from == null) return;
		for (int i = 0; i < from.getSlotCount(); i++) {
			ItemStack upgrade = from.getUpgrade(i);
			to.setUpgrade(i, upgrade);
		}
	}

	private static void copyAttributes(@Nullable IDrawerAttributes from, IDrawerAttributesModifiable to) {
		if (from == null) return;
		to.setIsConcealed(from.isConcealed());
		to.setItemLocked(LockAttribute.LOCK_EMPTY, from.isItemLocked(LockAttribute.LOCK_EMPTY));
		to.setItemLocked(LockAttribute.LOCK_POPULATED, from.isItemLocked(LockAttribute.LOCK_POPULATED));
		to.setIsShowingQuantity(from.isShowingQuantity());
		to.setIsSealed(from.isSealed());
		to.setIsVoid(from.isVoid());
		to.setIsUnlimitedStorage(from.isUnlimitedStorage());
		to.setIsUnlimitedVending(from.isUnlimitedVending());
		to.setIsDictConvertible(from.isDictConvertible());
	}

	@Override
	public void setSize(int size) {
		super.setSize(size);
	}

	private void copyItems(@Nullable IDrawerGroup from, IDrawerGroup to) {
		if (from == null) return;
		if (from.getDrawerCount() != to.getDrawerCount())
			throw new IllegalArgumentException("Provided drawers dimensions mismatch");
		for (int i = 0; i < from.getDrawerCount(); i++) {
			IDrawer dFrom = from.getDrawer(i);
			to.getDrawer(i).setStoredItem(dFrom.getStoredItemPrototype(),
					dFrom.getStoredItemCount());
		}
	}

	public void copyItemsTo(TileEntityDrawersStandard drawer) {
		copyItems(drawerGroup, drawer.getGroup());
	}

	@Nonnull
	private IDrawerAttributes getDrawerAttributes() {
		return drawerAttributes;
	}

	@Nonnull
	@Override
	public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> cap, @Nullable Direction side) {
		return cap == DRAWER_ATTRIBUTES_CAPABILITY ? attributesHandler.cast() : LazyOptional.empty();
	}

	@Override
	public void setStackInSlot(int slot, @Nonnull ItemStack stack) {
		validateSlotIndex(slot);
		IDrawer drawer = drawerGroup.getDrawer(slot);
		drawer.setStoredItem(stack, stack.getCount());
	}

	@Override
	public int getSlots() {
		return drawerGroup.getDrawerCount();
	}

	@Nonnull
	@Override
	public ItemStack getStackInSlot(int slot) {
		IDrawer drawer = drawerGroup.getDrawer(slot);
		int count = drawer.getStoredItemCount();
		if (count <= 0) return ItemStack.EMPTY;
		return ItemHandlerHelper.copyStackWithSize(drawer.getStoredItemPrototype(), count);
	}

	@Nonnull
	@Override
	public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
		IDrawer drawer = drawerGroup.getDrawer(slot);
		if (stack.isEmpty())
			return ItemStack.EMPTY;
		if (!isItemValid(slot, stack))
			return stack;

		int max = drawer.getMaxCapacity(stack);
		int cur = drawer.getStoredItemCount();
		int limit = max - cur;
		if (drawerAttributes.isVoid()) limit = Integer.MAX_VALUE;

		if (limit <= 0) return stack;
		boolean reachedLimit = stack.getCount() > limit;
		if (!simulate) {
			drawer.setStoredItem(stack, Math.min(drawer.getMaxCapacity(), drawer.getStoredItemCount() + stack.getCount()));
		}

		return reachedLimit ? ItemHandlerHelper.copyStackWithSize(stack, stack.getCount() - limit) : ItemStack.EMPTY;
	}

	@Nonnull
	@Override
	public ItemStack extractItem(int slot, int amount, boolean simulate) {
		if (amount == 0)
			return ItemStack.EMPTY;

		validateSlotIndex(slot);

		IDrawer drawer = drawerGroup.getDrawer(slot);

		int cur = drawer.getStoredItemCount();
		ItemStack existing = drawer.getStoredItemPrototype();
		if (existing.isEmpty() || cur == 0) return ItemStack.EMPTY;

		int toExtract = Math.min(amount, existing.getMaxStackSize());
		if (cur <= toExtract) {
			if (!simulate) {
				drawer.setStoredItemCount(0);
			}
			return ItemHandlerHelper.copyStackWithSize(existing, cur);
		} else {
			if (!simulate) {
				drawer.setStoredItemCount(cur - toExtract);
			}
			return ItemHandlerHelper.copyStackWithSize(existing, toExtract);
		}
	}

	@Override
	public int getSlotLimit(int slot) {
		IDrawer drawer = drawerGroup.getDrawer(slot);
		return drawerAttributes.isVoid() ? Integer.MAX_VALUE : drawer.getMaxCapacity();
	}

	@Override
	protected int getStackLimit(int slot, @Nonnull ItemStack stack) {
		IDrawer drawer = drawerGroup.getDrawer(slot);
		return drawerAttributes.isVoid() ? Integer.MAX_VALUE : drawer.getMaxCapacity(stack);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		IDrawer drawer = drawerGroup.getDrawer(slot);
		return drawer.canItemBeStored(stack);
	}

	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= getSlots())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
	}

	@Override
	public CompoundNBT serializeNBT() {
		CompoundNBT nbt = super.serializeNBT();
		ContraptionStorageRegistry.serializeClassName(nbt, TileEntityDrawersStandard.class);

		nbt.putInt("StorageUnits", storageUnits);
		nbt.putInt("DrawersAmount", drawers);
		nbt.put("Upgrades", upgradeData.serializeNBT());
		nbt.put("Attributes", drawerAttributes.serializeNBT());
		nbt.put("DrawerGroup", drawerGroup.serializeNBT());
		return nbt;
	}

	@Override
	public void deserializeNBT(CompoundNBT nbt) {
		super.deserializeNBT(nbt);

		storageUnits = nbt.getInt("StorageUnits");
		drawers = nbt.getInt("DrawersAmount");
		upgradeData.deserializeNBT(nbt.getCompound("Upgrades"));
		drawerAttributes.deserializeNBT(nbt.getCompound("Attributes"));
		drawerGroup = new ContraptionDrawerGroup(drawers);
		drawerGroup.deserializeNBT(nbt.getCompound("DrawerGroup"));
	}

	public class ContraptionDrawerGroup extends StandardDrawerGroup {

		public ContraptionDrawerGroup(int slotCount) {
			super(slotCount);
			setCapabilityProvider(StorageDrawerHandler.this);
		}

		@Nonnull
		@Override
		protected DrawerData createDrawer(int i) {
			return new ContraptionDrawerData(this);
		}

		@Override
		public boolean isGroupValid() {
			return true;
		}
	}

	private class ContraptionDrawerData extends StandardDrawerGroup.DrawerData {

		public ContraptionDrawerData(StandardDrawerGroup group) {
			super(group);
		}

		@Override
		protected int getStackCapacity() {
			return upgradeData.getStorageMultiplier() *
					(upgradeData.hasOneStackUpgrade() ? 1 :
							storageUnits * CommonConfig.GENERAL.baseStackStorage.get());
		}
	}
}
