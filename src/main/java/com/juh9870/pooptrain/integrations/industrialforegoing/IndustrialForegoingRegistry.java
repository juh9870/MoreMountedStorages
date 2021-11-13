package com.juh9870.pooptrain.integrations.industrialforegoing;

import com.buuz135.industrial.utils.Reference;
import com.juh9870.pooptrain.Config;
import com.juh9870.pooptrain.ContraptionItemStackHandler;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.juh9870.pooptrain.helpers.AdvancedItemStackHandler;
import net.minecraft.item.Rarity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.ObjectHolder;

import java.util.ArrayList;
import java.util.List;

public class IndustrialForegoingRegistry extends ContraptionStorageRegistry {

	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance("industrialforegoing:black_hole_unit");
	private static final Lazy<TileEntityType<?>[]> affectedStorages = Lazy.of(() -> {
		List<TileEntityType<?>> values = new ArrayList<>();

		for (Rarity rarity : Rarity.values()) {
			TileEntityType<?> type = ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation(Reference.MOD_ID, rarity.name().toLowerCase() + "_black_hole_unit"));
			if (type != null) values.add(type);
		}

		return values.toArray(new TileEntityType<?>[0]);
	});


	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && Config.INDUSTRIAL_FOREGOING_UNIT.get();
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return affectedStorages.get();
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		IItemHandler bhHandler = getHandlerFromDefaultCapability(te);
		if (bhHandler == dummyHandler) {
			return null;
		}
		boolean voiding = te.serializeNBT().getBoolean("voidItems");
		return new BlackHoleItemStackHandler(bhHandler).setVoiding(voiding);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return deserializeHandler(new BlackHoleItemStackHandler(), nbt);
	}

	public static class BlackHoleItemStackHandler extends AdvancedItemStackHandler {
		public BlackHoleItemStackHandler() {
			setIgnoreItemStackSize(true);
			setVoiding(true);
		}

		public BlackHoleItemStackHandler(IItemHandler handler) {
			super(handler);
			setIgnoreItemStackSize(true);
			setVoiding(true);
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
	}
}
