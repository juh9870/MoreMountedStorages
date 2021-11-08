package com.juh9870.pooptrain;

import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.util.HashMap;
import java.util.Map;

public abstract class ContraptionStorageRegistry {
	public static final String CLASS_NAME = "pooptrain_classname";
	private static final Map<Class<?>, ContraptionStorageRegistry> storages = new HashMap<>();

	public static boolean canUseAsStorage(TileEntity te) {
		return storages.containsKey(te.getClass());
	}

	public static ContraptionStorageRegistry getStorage(TileEntity te) {
		PoopTrain.LOGGER.debug("Tile entity class requested: " + (te != null ? te.getClass() : null));
		return storages.get(te.getClass());
	}

	public static ContraptionStorageRegistry getStorage(Class<?> teClass) {
		return storages.get(teClass);
	}

	protected static void registerStorage(Class<?> teClass, ContraptionStorageRegistry registry) {
		storages.put(teClass, registry);
	}

	protected static void registerStorages(ContraptionStorageRegistry registry, Class<?>... tileEntities) {
		for (Class<?> tileEntity : tileEntities) {
			registerStorage(tileEntity, registry);
		}
	}

	public static void serializeClassName(CompoundNBT nbt, Class<? extends TileEntity> teClass) {
		nbt.putString(CLASS_NAME, teClass.getName());
	}

	/**
	 * Performs manipulations on tile entity before it's actually removed from a world, aka splitting double chests and returns true if storage is valid to be used contraption, or false if this storage shouldn't be used
	 *
	 * @param te original Tile Entity
	 * @return true if storage is valid to be used in contraption
	 */
	public boolean prepareStorageForContraption(TileEntity te) {
		return true;
	}

	/**
	 * Performs manipulations on Tile Entity when it's being added to the world after contraption disassembly and returns true if default logic of copying items from handler to inventory should be skipped;
	 *
	 * @param te Tile Entity being added to the world
	 * @return true if default create logic should be skipped
	 */
	public boolean addStorageToWorld(TileEntity te, ItemStackHandler handler) {
		return false;
	}

	/**
	 * Returns true if this storage uses custom subclass of {@link  net.minecraftforge.items.ItemStackHandler}. If true is returned, must also implement {@link ContraptionStorageRegistry#deserializeHandler(net.minecraft.nbt.CompoundNBT)} and {@link ContraptionStorageRegistry#getHandler(net.minecraft.tileentity.TileEntity)}
	 *
	 * @return true if this storage uses non-standard handler
	 */
	public abstract boolean useCustomHandler();

	/**
	 * Returns Item handler to be used in contraption, or null if storage is invalid and shouldn't be used asstorage in contraption
	 *
	 * @param te original Tile Entity
	 * @return Item handler to be used in contraption
	 */
	public ItemStackHandler getHandler(TileEntity te) {
		throw new NotImplementedException();
	}

	/**
	 * Returns {@link  net.minecraftforge.items.ItemStackHandler} deserialized from NBT
	 *
	 * @param nbt saved NBT
	 * @return deserialized handler
	 */
	public ItemStackHandler deserializeHandler(CompoundNBT nbt) {
		throw new NotImplementedException();
	}
}
