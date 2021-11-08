package com.juh9870.pooptrain;

import com.juh9870.pooptrain.integrations.enderstorage.EnderStorageRegistry;
import com.juh9870.pooptrain.integrations.ironchests.IronChestsRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.FramedCompactDrawersRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.StorageDrawersRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pooptrain")
public class PoopTrain {
	public static final Logger LOGGER = LogManager.getLogger();
	public static int managerGeneration = 0;

	public PoopTrain() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.addListener(PoopTrain::init);
	}

	public static void init(FMLCommonSetupEvent event) {
		if (ModList.get().isLoaded("enderstorage")) {
			EnderStorageRegistry.register();
		}
		if (ModList.get().isLoaded("ironchest")) {
			IronChestsRegistry.register();
		}
		if (ModList.get().isLoaded("storagedrawers")) {
			StorageDrawersRegistry.register();

			if (ModList.get().isLoaded("framedcompactdrawers")) {
				FramedCompactDrawersRegistry.register();
			}
		}
	}
}
