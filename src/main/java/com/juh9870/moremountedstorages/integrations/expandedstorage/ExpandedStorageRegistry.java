package com.juh9870.moremountedstorages.integrations.expandedstorage;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.Utils;
import com.juh9870.moremountedstorages.helpers.AdvancedItemStackHandler;
import com.simibubi.create.api.contraption.ContraptionItemStackHandler;
import com.simibubi.create.api.contraption.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import ninjaphenix.expandedstorage.base.internal_api.block.AbstractChestBlock;
import ninjaphenix.expandedstorage.base.internal_api.block.misc.CursedChestType;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ExpandedStorageRegistry extends ContraptionStorageRegistry {

	public static final Config.RegistryInfo CONFIG = new Config.RegistryInfo("chest", "Chests");
	private static final Lazy<TileEntityType<?>[]> affectedStorages = Lazy.of(() -> {
		List<TileEntityType<?>> values = new ArrayList<>();
		for (TileEntityType<?> tileEntity : ForgeRegistries.TILE_ENTITIES) {
			if (tileEntity.getRegistryName().getNamespace().equals("expandedstorage")) {
				values.add(tileEntity);
			}
		}

		return values.toArray(new TileEntityType<?>[0]);
	});
	public static Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("expandedstorage", "chest"));

	@SuppressWarnings("UnstableApiUsage")
	public static void makeSingle(TileEntity te, boolean updateNeighbour) {
		Optional<CursedChestType> type = te.getBlockState().getOptionalValue(AbstractChestBlock.CURSED_CHEST_TYPE);
		if (type.isPresent() && type.get() != CursedChestType.SINGLE) {
			Direction dir = AbstractChestBlock.getDirectionToAttached(te.getBlockState());
			te.getLevel()
					.setBlockAndUpdate(te.getBlockPos(), te.getBlockState()
							.setValue(AbstractChestBlock.CURSED_CHEST_TYPE, CursedChestType.SINGLE));
			if (updateNeighbour) {
				TileEntity other = te.getLevel().getBlockEntity(te.getBlockPos().offset(dir.getNormal()));
				if (other != null) {
					makeSingle(other, false);
				}
			}
		}
		te.clearCache();
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		makeSingle(te, true);
		return new ChestSplittingHandler(getHandlerFromDefaultCapability(te));
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return deserializeHandler(new ChestSplittingHandler(), nbt);
	}

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return affectedStorages.get();
	}

	public static class ChestSplittingHandler extends AdvancedItemStackHandler {
		public ChestSplittingHandler() {
		}

		public ChestSplittingHandler(IItemHandler handler) {
			super(handler);
		}

		@Override
		public int getPriority() {
			return 0;
		}

		@Override
		protected ContraptionStorageRegistry registry() {
			return INSTANCE.get();
		}

		@Override
		public boolean addStorageToWorld(TileEntity te) {
			makeSingle(te, true);
			return true;
		}
	}
}
