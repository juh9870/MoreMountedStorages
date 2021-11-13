package com.juh9870.moremountedstorages;

import com.juh9870.moremountedstorages.integrations.enderstorage.EnderStorageRegistry;
import com.juh9870.moremountedstorages.integrations.immersiveengineering.ImmersiveEngineeringRegistry;
import com.juh9870.moremountedstorages.integrations.industrialforegoing.IndustrialForegoingControllerRegistry;
import com.juh9870.moremountedstorages.integrations.industrialforegoing.IndustrialForegoingRegistry;
import com.juh9870.moremountedstorages.integrations.ironchests.IronChestsRegistry;
import com.juh9870.moremountedstorages.integrations.pneumaticcraft.PneumaticcraftRegistry;
import com.juh9870.moremountedstorages.integrations.storagedrawers.CompactingDrawerRegistry;
import com.juh9870.moremountedstorages.integrations.storagedrawers.FramedCompactingDrawerRegistry;
import com.juh9870.moremountedstorages.integrations.storagedrawers.FramedDrawersRegistry;
import com.juh9870.moremountedstorages.integrations.storagedrawers.StorageDrawersRegistry;
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
@Mod("moremountedstorages")
public class MoreMountedStorages {
	public static final Logger LOGGER = LogManager.getLogger();

	public MoreMountedStorages() {
		IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
		modEventBus.register(this);
		ContraptionStorageRegistry.STORAGES.register(modEventBus);
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "moremountedstorages-common.toml");
	}

	public static void breakpoint() {
		LOGGER.debug("POOP!");
	}

	@SubscribeEvent
	public void registerModules(RegistryEvent.Register<ContraptionStorageRegistry> event) {
		IForgeRegistry<ContraptionStorageRegistry> registry = event.getRegistry();
		ContraptionStorageRegistry.registerIfModLoaded(registry, "enderstorage", "moremountedstorages:enderstorage_ender_chest", () -> new EnderStorageRegistry());
		ContraptionStorageRegistry.registerIfModLoaded(registry, "ironchest", "moremountedstorages:ironchest_chest", () -> new IronChestsRegistry());
		registerStorageDrawers(registry);
		ContraptionStorageRegistry.registerIfModLoaded(registry, "immersiveengineering", "moremountedstorages:immersiveengineering_crate", () -> new ImmersiveEngineeringRegistry());
		registerIndustrialForegoing(registry);
		registerPneumaticcraft(registry);
	}

	private void registerStorageDrawers(IForgeRegistry<ContraptionStorageRegistry> registry) {
		ContraptionStorageRegistry.registerIfModLoaded(registry, "storagedrawers", "moremountedstorages:storagedrawers_drawer", () -> new StorageDrawersRegistry());
		ContraptionStorageRegistry.registerIfModLoaded(registry, "storagedrawers", "moremountedstorages:storagedrawers_compacting_drawer", () -> new CompactingDrawerRegistry());

		ContraptionStorageRegistry.registerIfModLoaded(registry, "framedcompactdrawers", "moremountedstorages:framedcompactdrawers_drawer", () -> new FramedDrawersRegistry());
		ContraptionStorageRegistry.registerIfModLoaded(registry, "framedcompactdrawers", "moremountedstorages:framedcompactdrawers_compacting_drawer", () -> new FramedCompactingDrawerRegistry());
	}

	private void registerIndustrialForegoing(IForgeRegistry<ContraptionStorageRegistry> registry) {
		ContraptionStorageRegistry.registerIfModLoaded(registry, "industrialforegoing", "moremountedstorages:industrialforegoing_black_hole_unit", () -> new IndustrialForegoingRegistry());
		ContraptionStorageRegistry.registerIfModLoaded(registry, "industrialforegoing", "moremountedstorages:industrialforegoing_black_hole_controller", () -> new IndustrialForegoingControllerRegistry());
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
		}, "moremountedstorages:pneumaticcraft_smart_chest", () -> new PneumaticcraftRegistry());
	}
}
