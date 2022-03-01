package com.juh9870.moremountedstorages.integrations.create;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.CrateItemHandler;
import com.simibubi.create.AllTileEntities;
import com.simibubi.create.content.logistics.block.inventories.AdjustableCrateBlock;
import com.simibubi.create.content.logistics.block.inventories.AdjustableCrateTileEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;

public class FlexCrateRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("create", "crate"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("crate", "Create Crate", 0);

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				AllTileEntities.ADJUSTABLE_CRATE.get()
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		// Split double flexcrates
		if (te.getBlockState()
				.getValue(AdjustableCrateBlock.DOUBLE))
			te.getLevel()
					.setBlockAndUpdate(te.getBlockPos(), te.getBlockState()
							.setValue(AdjustableCrateBlock.DOUBLE, false));
		te.clearCache();

		return new FlexCrateHandler((AdjustableCrateTileEntity) te);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return deserializeHandler(new FlexCrateHandler(), nbt);
	}

	public static class FlexCrateHandler extends CrateItemHandler {
		public FlexCrateHandler() {
		}

		public FlexCrateHandler(AdjustableCrateTileEntity te) {
			this(te.allowedAmount);
		}

		public FlexCrateHandler(int allowedAmount) {
			super(allowedAmount);
		}

		@Override
		protected ContraptionStorageRegistry registry() {
			return INSTANCE.get();
		}

		@Override
		public int getPriority() {
			return CONFIG.getPriority();
		}
	}
}
