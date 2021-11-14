package com.juh9870.moremountedstorages;

import com.simibubi.create.Create;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.ModList;
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

	public static void initCache() throws RegistryConflictException {
		if (tileEntityMappingsCache != null) return;
		tileEntityMappingsCache = new HashMap<>();
		for (ContraptionStorageRegistry registry : REGISTRY.get()) {
			ContraptionStorageRegistry other;
			for (TileEntityType<?> tileEntityType : registry.affectedStorages()) {
				if ((other = tileEntityMappingsCache.get(tileEntityType)) != null) {
					if (other.getPriority() == registry.getPriority() && other.getPriority() != Priority.DUMMY) {
						throw new RegistryConflictException(tileEntityType, other.getClass(), registry.getClass());
					} else if (!registry.getPriority().isOverwrite(other.getPriority()))
						continue;
				}

				tileEntityMappingsCache.put(tileEntityType, registry);
			}
		}
	}

	/**
	 * Returns registry entry that handles provided entity type, or null if no matching entry found
	 *
	 * @param type Type of tile entity
	 * @return matching registry entry, or null if nothing is found
	 */
	@Nullable
	public static ContraptionStorageRegistry forTileEntity(TileEntityType<?> type) throws RegistryConflictException {
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

	/**
	 * Helper method for getting registry instance
	 *
	 * @param id registry name
	 * @return Lazy with given name
	 */
	protected static Lazy<ContraptionStorageRegistry> getInstance(String id) {
		return Lazy.of(() -> REGISTRY.get().getValue(new ResourceLocation(id)));
	}

	public abstract Priority getPriority();

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
	 * Registry priority enum
	 * <p>
	 * Registries with addon priority are overwritten by registries with native priority
	 * </p>
	 */
	public enum Priority {
		/**
		 * Dummy priority, use this in case if your registry is a dummy and should be overwritten by any better option
		 */
		DUMMY {
			@Override
			public boolean isOverwrite(Priority other) {
				return true;
			}
		},
		/**
		 * Add-on priority, use this if your registry is coming from an add-on to the external mod
		 */
		ADDON {
			@Override
			public boolean isOverwrite(Priority other) {
				return other == DUMMY;
			}
		},
		/**
		 * Native mod priority, use this if your registry is a part of the mod it's adding support to
		 */
		NATIVE {
			@Override
			public boolean isOverwrite(Priority other) {
				return other != NATIVE;
			}
		};

		public abstract boolean isOverwrite(Priority other);
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
		public Priority getPriority() {
			return Priority.DUMMY;
		}

		@Override
		public TileEntityType<?>[] affectedStorages() {
			return new TileEntityType[0];
		}
	}

	public static class RegistryConflictException extends Exception {
		public RegistryConflictException(TileEntityType<?> teType, Class<? extends ContraptionStorageRegistry> a, Class<? extends ContraptionStorageRegistry> b) {
			super("Registry conflict: registries " + a.getName() + " and " + b.getName() + " tried to register the same tile entity " + teType.getRegistryName());
		}
	}
}
