package com.juh9870.moremountedstorages;

import com.simibubi.create.Create;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistryEntry;
import net.minecraftforge.registries.IForgeRegistry;
import net.minecraftforge.registries.RegistryBuilder;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public abstract class ContraptionStorageRegistry extends ForgeRegistryEntry<ContraptionStorageRegistry> {
	public static final ItemStackHandler dummyHandler = new ItemStackHandler();

	public static final DeferredRegister<ContraptionStorageRegistry> STORAGES = DeferredRegister.create(ContraptionStorageRegistry.class, Create.ID);
	public static final Supplier<IForgeRegistry<ContraptionStorageRegistry>> REGISTRY = STORAGES.makeRegistry("mountable_storage", RegistryBuilder::new);

	public static final String REGISTRY_NAME = "StorageRegistryId";
	private static Map<TileEntityType<?>, ContraptionStorageRegistry> tileEntityMappingsCache = null;

	/**
	 * Returns registry entry that handles provided entity type, or null if no matching entry found
	 *
	 * @param type Type of tile entity
	 * @return matching registry entry, or null if nothing is found
	 */
	@Nullable
	public static ContraptionStorageRegistry forTileEntity(TileEntityType<?> type) {
		if (tileEntityMappingsCache == null) {
			tileEntityMappingsCache = new HashMap<>();
			for (ContraptionStorageRegistry registry : REGISTRY.get()) {
				for (TileEntityType<?> tileEntityType : registry.affectedStorages()) {
					tileEntityMappingsCache.put(tileEntityType, registry);
				}
			}
		}
		return tileEntityMappingsCache.get(type);
	}

	/**
	 * Helper method to conditionally register handlers. Registers value from {@code supplier} parameter if {@code condition} returns true, otherwise generates new {@link DummyHandler} and registers it
	 *
	 * @param registry     registry for entry registering
	 * @param condition    Loading condition supplier
	 * @param registryName Name to register the entry under
	 * @param supplier     Supplier to get the entry
	 */
	public static void registerConditionally(IForgeRegistry<ContraptionStorageRegistry> registry, Supplier<Boolean> condition, String registryName, Supplier<ContraptionStorageRegistry> supplier) {
		ContraptionStorageRegistry entry;
		if (condition.get()) {
			entry = supplier.get().setRegistryName(registryName);
		} else {
			entry = new DummyHandler().setRegistryName(registryName);
		}
		registry.register(entry);
	}


	/**
	 * Helper method to conditionally register handlers based on if specified mod is loaded. Registers value from {@code supplier} parameter if specified mod is loaded, otherwise generates new {@link DummyHandler} and registers it
	 *
	 * @param registry     registry for entry registering
	 * @param modid        Required mod ID
	 * @param registryName Name to register the entry under
	 * @param supplier     Supplier to get the entry
	 */
	public static void registerIfModLoaded(IForgeRegistry<ContraptionStorageRegistry> registry, String modid, String registryName, Supplier<ContraptionStorageRegistry> supplier) {
		registerConditionally(registry, () -> ModList.get().isLoaded(modid), registryName, supplier);
	}

	/**
	 * Helper method to get default item handler capability from tile entity
	 *
	 * @param te TileEntity to get handler from
	 * @return IItemHandler from {@link CapabilityItemHandler#ITEM_HANDLER_CAPABILITY} capability
	 */
	protected static IItemHandler getHandlerFromDefaultCapability(TileEntity te) {
		return te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(dummyHandler);
	}

	protected static Lazy<ContraptionStorageRegistry> getInstance(String id) {
		return Lazy.of(() -> REGISTRY.get().getValue(new ResourceLocation(id)));
	}

	/**
	 * @return array of Tile Entity types handled by this registry
	 */
	public abstract TileEntityType<?>[] affectedStorages();

	/**
	 * @param te Tile Entity
	 * @return true if this tile entity can be used as storage
	 */
	public boolean canUseAsStorage(TileEntity te) {
		return true;
	}

	/**
	 * Returns Item handler to be used in contraption, or null if default logic should be used
	 *
	 * @param te original Tile Entity
	 * @return Item handler to be used in contraption or null if default logic should be used
	 */
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		return null;
	}

	/**
	 * Returns {@link  ContraptionItemStackHandler} deserialized from NBT
	 *
	 * @param nbt saved NBT
	 * @return deserialized handler
	 */
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		throw new NotImplementedException();
	}

	protected final ContraptionItemStackHandler deserializeHandler(ContraptionItemStackHandler handler, CompoundNBT nbt) {
		handler.deserializeNBT(nbt);
		return handler;
	}

	/**
	 * {@link  ContraptionItemStackHandler} is provided with World after initialisation and after deserialization
	 */
	public interface IWorldRequiringHandler {
		void applyWorld(World world);

		default void applyWorldOnDeserialization(World world) {
			applyWorld(world);
		}
	}


	public static class DummyHandler extends ContraptionStorageRegistry {
		@Override
		public boolean canUseAsStorage(TileEntity te) {
			return false;
		}

		@Override
		public TileEntityType<?>[] affectedStorages() {
			return new TileEntityType[0];
		}
	}
}
