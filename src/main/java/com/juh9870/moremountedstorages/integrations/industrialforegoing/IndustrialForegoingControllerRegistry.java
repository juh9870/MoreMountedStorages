package com.juh9870.moremountedstorages.integrations.industrialforegoing;

import com.buuz135.industrial.block.transportstorage.tile.BlackHoleControllerTile;
import com.buuz135.industrial.capability.BLHBlockItemHandlerItemStack;
import com.hrznstudio.titanium.component.inventory.InventoryComponent;
import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.FilteringItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;

import javax.annotation.Nonnull;

import static com.juh9870.moremountedstorages.ContraptionItemStackHandler.PRIORITY_ITEM_BIN;

public class IndustrialForegoingControllerRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("industrialforegoing", "black_hole_controller"));
		public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("black_hole_controller", "Black Hole Controller", PRIORITY_ITEM_BIN);

	private static final Lazy<TileEntityType<?>[]> affectedStorages = Lazy.of(() -> new TileEntityType<?>[]{ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation("industrialforegoing:black_hole_controller"))});

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return deserializeHandler(new BlackHoleControllerItemStackHandler(), nbt);
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && CONFIG.isEnabled();
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return affectedStorages.get();
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		BlackHoleControllerTile bhc = (BlackHoleControllerTile) te;
		IItemHandler bhHandler = getHandlerFromDefaultCapability(te);
		if (bhHandler == dummyHandler) {
			return null;
		}

		BlackHoleControllerItemStackHandler handler = new BlackHoleControllerItemStackHandler(bhHandler);

		InventoryComponent<BlackHoleControllerTile> units = bhc.getUnitsStorage();
		for (int i = 0; i < units.getSlots(); i++) {
			ItemStack unit = units.getStackInSlot(i);
			if (unit.isEmpty()) continue;
			IItemHandler unitHandler = unit.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(() -> dummyHandler);
			boolean voiding = true;
			if (unitHandler instanceof BLHBlockItemHandlerItemStack) {
				handler.setFilter(i, ((BLHBlockItemHandlerItemStack) unitHandler).getStack());
				voiding = ((BLHBlockItemHandlerItemStack) unitHandler).getVoid();
			}
			handler.setVoiding(i, voiding);
		}

		return handler;
	}

	public static class BlackHoleControllerItemStackHandler extends FilteringItemStackHandler {
		public BlackHoleControllerItemStackHandler() {
			setIgnoreItemStackSize(true);
		}

		public BlackHoleControllerItemStackHandler(IItemHandler handler) {
			super(handler);
			setIgnoreItemStackSize(true);
			for (int i = 0; i < stacks.size(); i++) {
				setFilter(i, stacks.get(i));
			}
		}

		@Nonnull
		@Override
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate) {
			ItemStack returnStack = super.insertItem(slot, stack, simulate);
			if (!simulate && !getStackInSlot(slot).isEmpty()) {
				filter[slot] = getStackInSlot(slot).copy();
			}
			return returnStack;
		}

		@Override
		protected ContraptionStorageRegistry registry() {
			return INSTANCE.get();
		}

		@Override
		public boolean addStorageToWorld(TileEntity te) {
			IItemHandler bhHandler = getHandlerFromDefaultCapability(te);
			if (bhHandler == dummyHandler) {
				return false;
			}

			simpleOverwrite(bhHandler);
			return false;
		}

		@Override
		public int getPriority() {
			return CONFIG.getPriority();
		}
	}

}
