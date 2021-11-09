package com.juh9870.pooptrain.integrations.industrialforegoing;

import com.buuz135.industrial.block.transportstorage.tile.BlackHoleControllerTile;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.juh9870.pooptrain.helpers.VoidingItemStackHandler;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public class IndustrialForegoingControllerRegistry extends ContraptionStorageRegistry {
	public static final ItemStackHandler dummyHandler = new ItemStackHandler();

	public static void register() {
		registerStorages(new IndustrialForegoingControllerRegistry(),
				BlackHoleControllerTile.class
		);
	}

	@Override
	public boolean useCustomHandler() {
		return true;
	}

	@Override
	public ItemStackHandler getHandler(TileEntity te) {
		BlackHoleControllerTile bhu = (BlackHoleControllerTile) te;
		IItemHandler bhHandler = bhu.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(dummyHandler);
		if (bhHandler == dummyHandler) {
			return null;
		}

		return new VoidingItemStackHandler(bhHandler, true);
	}

	@Override
	public boolean addStorageToWorld(TileEntity te, ItemStackHandler handler) {
		BlackHoleControllerTile bhu = (BlackHoleControllerTile) te;
		IItemHandler bhHandler = bhu.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY).orElse(dummyHandler);
		if (bhHandler == dummyHandler) {
			return false;
		}

		((VoidingItemStackHandler) handler).simpleOverwrite(bhHandler);
		return false;
	}
}
