package com.juh9870.pooptrain.integrations.industrialforegoing;

import com.buuz135.industrial.capability.BLHBlockItemHandlerItemStack;
import com.juh9870.pooptrain.ContraptionItemStackHandler;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.juh9870.pooptrain.helpers.FilteringItemStackHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;

import javax.annotation.Nonnull;

public class IndustrialForegoingControllerRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = createIfModLoaded(
			"industrialforegoing",
			"industrialforegoing:black_hole_controller",
			IndustrialForegoingControllerRegistry::new
	);

	private static final Lazy<TileEntityType<?>[]> affectedStorages = Lazy.of(() -> {
		return new TileEntityType<?>[]{ForgeRegistries.TILE_ENTITIES.getValue(new ResourceLocation("industrialforegoing:black_hole_controller"))};
	});

	public static void register(IForgeRegistry<ContraptionStorageRegistry> registry) {
		registry.register(INSTANCE.get());
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return deserializeHandler(new BlackHoleControllerItemStackHandler(), nbt);
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

		BlackHoleControllerItemStackHandler handler = new BlackHoleControllerItemStackHandler(bhHandler);

		ItemStackHandler units = new ItemStackHandler();
		units.deserializeNBT((CompoundNBT) te.serializeNBT().get("units_storage"));
		for (int i = 0; i < units.getSlots(); i++) {
			ItemStack unit = units.getStackInSlot(i);
			if (unit.isEmpty()) continue;
			IItemHandler unitHandler = unit.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElseGet(() -> dummyHandler);
			if (unitHandler instanceof BLHBlockItemHandlerItemStack) {
				handler.setFilter(i, ((BLHBlockItemHandlerItemStack) unitHandler).getStack());
			}
			boolean voiding = !unit.hasTag() ||
					!unit.getTag().contains("BlockEntityTag") ||
					!unit.getTag().getCompound("BlockEntityTag").contains("voidItems") ||
					unit.getTag().getCompound("BlockEntityTag").getBoolean("voidItems");
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
	}

}
