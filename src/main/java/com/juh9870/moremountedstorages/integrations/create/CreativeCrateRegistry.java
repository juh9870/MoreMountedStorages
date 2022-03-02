package com.juh9870.moremountedstorages.integrations.create;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.InfiniteItemStackHandler;
import com.simibubi.create.AllTileEntities;
import com.simibubi.create.content.logistics.block.inventories.BottomlessItemHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

import java.util.function.Supplier;

public class CreativeCrateRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("create", "creative_crate"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("creative_crate", "Create Creative Crate", 0);
	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				AllTileEntities.CREATIVE_CRATE.get()
		};
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && CONFIG.isEnabled();
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return deserializeHandler(new CreativeCrateItemHandler(() -> ItemStack.EMPTY), nbt);
	}


	public static class CreativeCrateItemHandler extends InfiniteItemStackHandler {
		public CreativeCrateItemHandler(Supplier<ItemStack> suppliedItemStack) {
			super(suppliedItemStack);
		}

		@Override
		protected ContraptionStorageRegistry registry() {
			return INSTANCE.get();
		}

		@Override
		public int getPriority() {
			return super.getPriority();
		}
	}
}