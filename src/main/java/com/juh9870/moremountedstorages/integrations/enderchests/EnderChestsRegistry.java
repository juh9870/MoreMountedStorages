package com.juh9870.moremountedstorages.integrations.enderchests;

import com.juh9870.moremountedstorages.Config;
import com.juh9870.moremountedstorages.Utils;
import com.simibubi.create.api.contraption.ContraptionItemStackHandler;
import com.simibubi.create.api.contraption.ContraptionStorageRegistry;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.util.Lazy;
import shetiphian.enderchests.Values;
import shetiphian.enderchests.common.tileentity.TileEntityEnderChest;

public class EnderChestsRegistry extends ContraptionStorageRegistry {
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = getInstance(Utils.constructId("enderchests", "ender_chest"));
	public static final Config.PriorityRegistryInfo CONFIG = new Config.PriorityRegistryInfo("ender_chest", "Ender Chests", 1);
	public static int managerGeneration = 0;

	@Override
	public Priority getPriority() {
		return Priority.ADDON;
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return super.canUseAsStorage(te) && CONFIG.isEnabled() && getHandlerFromDefaultCapability(te) != dummyHandler;
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				Values.tileEnderChest
		};
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {
		return new EnderChestHandler((TileEntityEnderChest) te);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return deserializeHandler(new EnderChestHandler(), nbt);
	}
}
