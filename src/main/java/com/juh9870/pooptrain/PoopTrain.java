package com.juh9870.pooptrain;

import com.juh9870.pooptrain.integrations.enderstorage.EnderStorageRegistry;
import com.juh9870.pooptrain.integrations.immersiveengineering.ImmersiveEngineeringRegistry;
import com.juh9870.pooptrain.integrations.industrialforegoing.IndustrialForegoingControllerRegistry;
import com.juh9870.pooptrain.integrations.industrialforegoing.IndustrialForegoingRegistry;
import com.juh9870.pooptrain.integrations.ironchests.IronChestsRegistry;
import com.juh9870.pooptrain.integrations.pneumaticcraft.PneumaticcraftRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.CompactingDrawerRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.FramedCompactingDrawerRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.FramedDrawersRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.StorageDrawersRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The value here should match an entry in the META-INF/mods.toml file
@Mod("pooptrain")
public class PoopTrain {
	public static final Logger LOGGER = LogManager.getLogger();

	public PoopTrain() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.register(this);
		ContraptionStorageRegistry.STORAGES.register(modEventBus);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "pooptrain-common.toml");
	}

	public static void breakpoint() {
		LOGGER.debug("POOP!");
	}

	@SubscribeEvent
	public void registerModules(RegistryEvent.Register<ContraptionStorageRegistry> event) {
		IForgeRegistry<ContraptionStorageRegistry> registry = event.getRegistry();
		ContraptionStorageRegistry.registerIfModLoaded(registry, "enderstorage", "pooptrain:enderstorage_ender_chest", () -> new EnderStorageRegistry());
		ContraptionStorageRegistry.registerIfModLoaded(registry, "ironchest", "pooptrain:ironchest_chest", () -> new IronChestsRegistry());
		registerStorageDrawers(registry);
		ContraptionStorageRegistry.registerIfModLoaded(registry, "immersiveengineering", "pooptrain:immersiveengineering_crate", () -> new ImmersiveEngineeringRegistry());
		registerIndustrialForegoing(registry);
		registerPneumaticcraft(registry);
	}

	private void registerStorageDrawers(IForgeRegistry<ContraptionStorageRegistry> registry) {
		ContraptionStorageRegistry.registerIfModLoaded(registry, "storagedrawers", "pooptrain:storagedrawers_drawer", () -> new StorageDrawersRegistry());
		ContraptionStorageRegistry.registerIfModLoaded(registry, "storagedrawers", "pooptrain:storagedrawers_compacting_drawer", () -> new CompactingDrawerRegistry());

		ContraptionStorageRegistry.registerIfModLoaded(registry, "framedcompactdrawers", "pooptrain:framedcompactdrawers_drawer", () -> new FramedDrawersRegistry());
		ContraptionStorageRegistry.registerIfModLoaded(registry, "framedcompactdrawers", "pooptrain:framedcompactdrawers_compacting_drawer", () -> new FramedCompactingDrawerRegistry());
	}

	private void registerIndustrialForegoing(IForgeRegistry<ContraptionStorageRegistry> registry) {
		ContraptionStorageRegistry.registerIfModLoaded(registry, "industrialforegoing", "pooptrain:industrialforegoing_black_hole_unit", () -> new IndustrialForegoingRegistry());
		ContraptionStorageRegistry.registerIfModLoaded(registry, "industrialforegoing", "pooptrain:industrialforegoing_black_hole_controller", () -> new IndustrialForegoingControllerRegistry());
	}

	private void registerPneumaticcraft(IForgeRegistry<ContraptionStorageRegistry> registry) {
		ContraptionStorageRegistry.registerConditionally(registry, () -> {
			Optional<? extends ModContainer> container = ModList.get().getModContainerById("pneumaticcraft");
			if (container.isPresent()) {
				String versionCode = container.get().getModInfo().getVersion().getQualifier();
				Matcher matcher = Pattern.compile("(\\d+)$").matcher(versionCode);
				if (!matcher.find()) return false;
				int buildId = Integer.parseInt(matcher.group(1));
				return buildId >= 273;
			}
			return false;
		}, "pooptrain:pneumaticcraft_smart_chest", () -> new PneumaticcraftRegistry());
	}
}
