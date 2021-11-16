package com.juh9870.moremountedstorages.integrations.trashcans;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.TrashCanHandler;
import com.supermartijn642.trashcans.TrashCanTile;
import com.supermartijn642.trashcans.TrashCans;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.Lazy;

import javax.annotation.Nonnull;
import java.util.ArrayList;

public class TrashCansRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("trashcans", "can"));
	public static final TrashCanConfig CONFIG = new TrashCanConfig("trashcans", "Trash Cans");

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType<?>[]{TrashCans.item_trash_can_tile, TrashCans.ultimate_trash_can_tile};
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te);
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		TrashCanTile can = (TrashCanTile) te;
		return new FilteredTrashCanHandler(can.itemFilter, can.itemFilterWhitelist);
	}

	public static class FilteredTrashCanHandler extends TrashCanHandler {
		private ArrayList<ItemStack> itemFilter = new ArrayList<>();
		private boolean whitelist;

		public FilteredTrashCanHandler() {
			for (int i = 0; i < 9; ++i) {
				this.itemFilter.add(ItemStack.EMPTY);
			}
		}

		public FilteredTrashCanHandler(ArrayList<ItemStack> itemFilter, boolean whitelist) {
			this();
			this.itemFilter = new ArrayList<>(itemFilter);
			this.whitelist = whitelist;
		}

		@Nonnull
		public ItemStack insertItem(int slot, @Nonnull ItemStack stack, boolean simulate){
			for(ItemStack filter : itemFilter){
				if(!filter.isEmpty() && ItemStack.isSame(stack, filter))
					return whitelist ? ItemStack.EMPTY : stack;
			}
			return whitelist ? stack : ItemStack.EMPTY;
		}

		public boolean isItemValid(int slot, @Nonnull ItemStack stack) {
			for(ItemStack filter : itemFilter){
				if(!filter.isEmpty() && ItemStack.isSame(stack, filter))
					return whitelist;
			}
			return !whitelist;
		}

		@Override
		public int getPriority() {
			return whitelist ? CONFIG.getWhitelistPriority() : CONFIG.getPriority();
		}

		@Override
		public CompoundNBT serializeNBT() {
			CompoundNBT nbt = super.serializeNBT();

			for (int i = 0; i < this.itemFilter.size(); ++i) {
				nbt.put("itemFilter" + i, this.itemFilter.get(i).save(new CompoundNBT()));
			}

			nbt.putBoolean("itemFilterWhitelist", whitelist);

			return nbt;
		}

		@Override
		public void deserializeNBT(CompoundNBT nbt) {
			super.deserializeNBT(nbt);
			for (int i = 0; i < this.itemFilter.size(); ++i) {
				this.itemFilter.set(i, nbt.contains("itemFilter" + i) ? ItemStack.of(nbt.getCompound("itemFilter" + i)) : ItemStack.EMPTY);
			}

			whitelist = nbt.contains("itemFilterWhitelist") && nbt.getBoolean("itemFilterWhitelist");
		}
	}

	public static class TrashCanConfig extends Config.PriorityRegistryInfo {

		private ForgeConfigSpec.ConfigValue<Integer> priorityWhitelist = null;

		public TrashCanConfig(String id, String name) {
			super(id, name, ContraptionItemStackHandler.PRIORITY_TRASH);
		}

		@Override
		protected void registerEntries(ForgeConfigSpec.Builder builder) {
			super.registerEntries(builder);
			priorityWhitelist = builder.comment(name + " storage priority when whitelist is enabled. Items are inserted first into storages with higher priority. Default value is 20").define("priority_whitelist", ContraptionItemStackHandler.PRIORITY_WHITELIST_TRASH);
		}

		public Integer getWhitelistPriority() {
			return priorityWhitelist != null ? priorityWhitelist.get() : -1;
		}
	}
}
