package com.juh9870.moremountedstorages.integrations.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawer;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerAttributesModifiable;
import com.jaquadro.minecraft.storagedrawers.api.storage.IDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawersStandard;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.StandardDrawerGroup;
import com.jaquadro.minecraft.storagedrawers.block.tile.tiledata.UpgradeData;
import com.jaquadro.minecraft.storagedrawers.capabilities.BasicDrawerAttributes;
import com.jaquadro.minecraft.storagedrawers.config.CommonConfig;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.capabilities.CapabilityToken;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class StorageDrawerHandler extends ContraptionItemStackHandler implements ICapabilityProvider {
	public static Capability<IDrawerAttributes> DRAWER_ATTRIBUTES_CAPABILITY = CapabilityManager.get(new CapabilityToken<>(){});
	protected final UpgradeData upgradeData;
	protected final BasicDrawerAttributes drawerAttributes;
	protected final LazyOptional<?> attributesHandler = LazyOptional.of(this::getDrawerAttributes);
	protected IDrawerGroup drawerGroup;
	protected int storageUnits;
	protected int drawers;
	protected Level world;

	public StorageDrawerHandler() {
		this(new UpgradeData(7), new BasicDrawerAttributes(), null, 1, null);
	}

	public StorageDrawerHandler(UpgradeData upgrades, IDrawerAttributes attributes, @Nullable IDrawerGroup group, int units, @Nullable Level world) {
		super();
		this.storageUnits = units;
		this.world = world;

		upgradeData = new UpgradeData(upgrades.getSlotCount());
		copyUpgrades(upgrades, upgradeData);

		drawerAttributes = new BasicDrawerAttributes();
		copyAttributes(attributes, drawerAttributes);

		upgradeData.setDrawerAttributes(drawerAttributes);

		drawers = group == null ? 4 : group.getDrawerCount();
		drawerGroup = createGroup(drawers);
		if (group != null) copyItems(group, drawerGroup);

	}

	public StorageDrawerHandler(TileEntityDrawers drawer) {
		this(drawer.upgrades(), drawer.getDrawerAttributes(), drawer.getGroup(), drawer.getDrawerCapacity(), drawer.getLevel());
	}

	public static void copyUpgrades(@Nullable UpgradeData from, UpgradeData to) {
		if (from == null) return;
		for (int i = 0; i < from.getSlotCount(); i++) {
			ItemStack upgrade = from.getUpgrade(i);
			to.setUpgrade(i, upgrade);
		}
	}

	public static void copyAttributes(@Nullable IDrawerAttributes from, IDrawerAttributesModifiable to) {
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

	public void copyItems(IDrawerGroup from, IDrawerGroup to) {
		if (from == null) return;
		if (from.getDrawerCount() != to.getDrawerCount())
			throw new IllegalArgumentException("Provided drawers dimensions mismatch");
		for (int i = 0; i < from.getDrawerCount(); i++) {
			IDrawer dFrom = from.getDrawer(i);
			ItemStack stack = dFrom.getStoredItemPrototype();
			to.getDrawer(i).setStoredItem(stack,
					dFrom.getStoredItemCount());
		}
	}

	public void clearGroup(IDrawerGroup group) {
		for (int i = 0; i < group.getDrawerCount(); i++) {
			group.getDrawer(i).setStoredItem(ItemStack.EMPTY);
		}
	}

	public void copyItemsTo(@Nonnull TileEntityDrawers drawer) {
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

		int max = drawer.getAcceptingMaxCapacity(stack);
		int cur = drawer.getStoredItemCount();
		int limit = max - cur;
		if (drawerAttributes.isVoid() && limit == 0) {
			return ItemStack.EMPTY;
		}

		if (limit <= 0) return stack;
		boolean reachedLimit = stack.getCount() > limit;
		if (!simulate) {
			drawer.setStoredItem(stack);
			drawer.setStoredItemCount(Math.min(drawer.getMaxCapacity(), cur + stack.getCount()));
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
		return drawer.getAcceptingMaxCapacity(stack);
	}

	@Override
	public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
		try {
			IDrawer drawer = drawerGroup.getDrawer(slot);
			return drawer.canItemBeStored(stack);
		} catch (NullPointerException e) {
			return false;
		}
	}

	protected void validateSlotIndex(int slot) {
		if (slot < 0 || slot >= getSlots())
			throw new RuntimeException("Slot " + slot + " not in valid range - [0," + stacks.size() + ")");
	}

	@Override
	public int getPriority() {
		return StorageDrawersRegistry.CONFIG.getPriority();
	}

	@Override
	@SuppressWarnings("unchecked")
	public CompoundTag serializeNBT() {
		CompoundTag nbt = super.serializeNBT();

		nbt.putInt("StorageUnits", storageUnits);
		nbt.putInt("DrawersAmount", drawers);
		nbt.put("Upgrades", upgradeData.serializeNBT());
		nbt.put("Attributes", drawerAttributes.serializeNBT());
		nbt.put("DrawerGroup", ((INBTSerializable<CompoundTag>) drawerGroup).serializeNBT());
		return nbt;
	}

	@Override
	@SuppressWarnings("unchecked")
	public void deserializeNBT(CompoundTag nbt) {
		super.deserializeNBT(nbt);

		storageUnits = nbt.getInt("StorageUnits");
		drawers = nbt.getInt("DrawersAmount");
		upgradeData.deserializeNBT(nbt.getCompound("Upgrades"));
		drawerAttributes.deserializeNBT(nbt.getCompound("Attributes"));
		drawerGroup = createGroup(drawers);
		((INBTSerializable<CompoundTag>) drawerGroup).deserializeNBT(nbt.getCompound("DrawerGroup"));
	}

	protected IDrawerGroup createGroup(int drawers) {
		return new ContraptionDrawerGroup(drawers);
	}

	@Override
	public void applyWorld(Level world) {
		this.world = world;
	}

	@Override
	public boolean addStorageToWorld(BlockEntity te) {
		TileEntityDrawersStandard drawer = (TileEntityDrawersStandard) te;
		copyItemsTo(drawer);
		return false;
	}

	@Override
	protected ContraptionStorageRegistry registry() {
		return StorageDrawersRegistry.INSTANCE.get();
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
