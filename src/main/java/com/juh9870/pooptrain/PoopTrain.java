package com.juh9870.pooptrain;

import com.juh9870.pooptrain.integrations.enderstorage.EnderStorageRegistry;
import com.juh9870.pooptrain.integrations.immersiveengineering.ImmersiveEngineeringRegistry;
import com.juh9870.pooptrain.integrations.industrialforegoing.IndustrialForegoingRegistry;
import com.juh9870.pooptrain.integrations.ironchests.IronChestsRegistry;
import com.juh9870.pooptrain.integrations.pneumaticcraft.PneumaticcraftRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.StorageDrawersRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pooptrain")
public class PoopTrain {
	public static final Logger LOGGER = LogManager.getLogger();

	public PoopTrain() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.register(this);
		ContraptionStorageRegistry.STORAGES.register(modEventBus);
	}

	@SubscribeEvent
	public void registerModules(RegistryEvent.Register<ContraptionStorageRegistry> event) {
		IForgeRegistry<ContraptionStorageRegistry> registry = event.getRegistry();
		EnderStorageRegistry.register(registry);
		IronChestsRegistry.register(registry);
		StorageDrawersRegistry.register(registry);
		ImmersiveEngineeringRegistry.register(registry);
		IndustrialForegoingRegistry.register(registry);
		PneumaticcraftRegistry.register(registry);
	}

	public static void breakpoint(){
		LOGGER.debug("POOP!");
	}
}
