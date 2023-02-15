package com.juh9870.moremountedstorages;

import com.juh9870.moremountedstorages.integrations.VanillaStorageRegistry;
import com.juh9870.moremountedstorages.integrations.create.CreativeCrateRegistry;
import com.juh9870.moremountedstorages.integrations.create.FlexCrateRegistry;
import com.juh9870.moremountedstorages.integrations.dimstorage.DimStorageRegistry;
import com.juh9870.moremountedstorages.integrations.enderchests.EnderChestsRegistry;
import com.juh9870.moremountedstorages.integrations.enderstorage.EnderStorageRegistry;
import com.juh9870.moremountedstorages.integrations.expandedstorage.ExpandedStorageRegistry;
import com.juh9870.moremountedstorages.integrations.immersiveengineering.ImmersiveEngineeringRegistry;
import com.juh9870.moremountedstorages.integrations.industrialforegoing.IndustrialForegoingControllerRegistry;
import com.juh9870.moremountedstorages.integrations.industrialforegoing.IndustrialForegoingRegistry;
import com.juh9870.moremountedstorages.integrations.ironchests.IronChestsRegistry;
import com.juh9870.moremountedstorages.integrations.pneumaticcraft.PneumaticcraftRegistry;
import com.juh9870.moremountedstorages.integrations.storagedrawers.*;
import com.juh9870.moremountedstorages.integrations.storageoverhaul.StorageBarrelRegistry;
import com.juh9870.moremountedstorages.integrations.storageoverhaul.StorageOverhaulRegistry;
import com.juh9870.moremountedstorages.integrations.trashcans.TrashCansRegistry;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nonnull;
import java.util.Optional;
import java.util.function.Supplier;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MoreMountedStorages.ID)
public class MoreMountedStorages {
    public static final Logger LOGGER = LogManager.getLogger();
    public static final String ID = "moremountedstorages";

    public MoreMountedStorages() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.register(this);
        ContraptionStorageRegistry.STORAGES.register(modEventBus);
    }

    public static void breakpoint() {
        LOGGER.debug("POOP!");
    }

    @SubscribeEvent
    public void commonSetup(FMLCommonSetupEvent event) {
        ContraptionStorageRegistry.initCache();
    }

    // Using method refs causes class loading issues when target mod isn't loaded, so we use lambdas instead
    @SuppressWarnings("Convert2MethodRef")
    @SubscribeEvent
    public void registerModules(@Nonnull RegistryEvent.Register<ContraptionStorageRegistry> event) {
        Config.BUILDER.comment("Mod integration config").push("Integration");

        Registry registry = new Registry(event.getRegistry());

        registry.register("minecraft", "storages", () -> new VanillaStorageRegistry(), () -> VanillaStorageRegistry.CONFIG);

        registry.register("create", "crate", () -> new FlexCrateRegistry(), () -> FlexCrateRegistry.CONFIG);
        registry.register("create", "creative_crate", () -> new CreativeCrateRegistry(), () -> CreativeCrateRegistry.CONFIG);

        registry.register("enderstorage", "ender_chest", () -> new EnderStorageRegistry(), () -> EnderStorageRegistry.CONFIG);
        registry.register("enderchests", "ender_chest", () -> new EnderChestsRegistry(), () -> EnderChestsRegistry.CONFIG);
        registry.register("ironchest", "chest", () -> new IronChestsRegistry(), () -> IronChestsRegistry.CONFIG);
        registerStorageDrawers(registry);
        registry.register("immersiveengineering", "crate", () -> new ImmersiveEngineeringRegistry(), () -> ImmersiveEngineeringRegistry.CONFIG);
        registerIndustrialForegoing(registry);
        registerPneumaticcraft(registry);
        registry.register("expandedstorage", "chest", () -> new ExpandedStorageRegistry(), () -> ExpandedStorageRegistry.CONFIG);
        registry.register("trashcans", "trashcan", () -> new TrashCansRegistry(), () -> TrashCansRegistry.CONFIG);
        registry.register("dimstorage", "dimensional_chest", () -> new DimStorageRegistry(), () -> DimStorageRegistry.CONFIG);

        registerStorageOverhaul(registry);

        Config.BUILDER.pop();
        Config.SPEC = Config.BUILDER.build();
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "moremountedstorages-common.toml");
    }

    @SuppressWarnings("Convert2MethodRef")
    private void registerStorageDrawers(@Nonnull Registry registry) {
        registry.register("storagedrawers", "drawer", () -> new StorageDrawersRegistry());
        registry.register("storagedrawers", "compacting_drawer", () -> new CompactingDrawerRegistry());

        registry.register("framedcompactdrawers", "drawer", () -> new FramedDrawersRegistry());
        registry.register("framedcompactdrawers", "compacting_drawer", () -> new FramedCompactingDrawerRegistry());

        registry.register("morestoragedrawers", "drawer", () -> new MoreDrawersRegistry());

        Config.registerConfigsIfModLoaded("storagedrawers", () -> Utils.arrayOf(
                StorageDrawersRegistry.CONFIG,
                CompactingDrawerRegistry.CONFIG
        ));
    }

    @SuppressWarnings("Convert2MethodRef")
    private void registerIndustrialForegoing(@Nonnull Registry registry) {
        registry.register("industrialforegoing", "black_hole_unit", () -> new IndustrialForegoingRegistry());
        registry.register("industrialforegoing", "black_hole_controller", () -> new IndustrialForegoingControllerRegistry());
        Config.registerConfigsIfModLoaded("industrialforegoing", () -> Utils.arrayOf(
                IndustrialForegoingRegistry.CONFIG,
                IndustrialForegoingControllerRegistry.CONFIG
        ));
    }

    @SuppressWarnings("Convert2MethodRef")
    private void registerPneumaticcraft(@Nonnull Registry registry) {
        registry.registerConditionally(() -> {
            Optional<? extends ModContainer> container = ModList.get().getModContainerById("pneumaticcraft");
            if (container.isPresent()) {
                String versionCode = container.get().getModInfo().getVersion().getQualifier();
                Matcher matcher = Pattern.compile("(\\d+)$").matcher(versionCode);
                if (!matcher.find()) return false;
                int buildId = Integer.parseInt(matcher.group(1));
                return buildId >= 273;
            }
            return false;
        }, Utils.constructId("pneumaticcraft", "smart_chest"), () -> new PneumaticcraftRegistry());
        Config.registerConfigsIfModLoaded("pneumaticcraft", () -> Utils.arrayOf(PneumaticcraftRegistry.CONFIG));
    }

    @SuppressWarnings("Convert2MethodRef")
    private void registerStorageOverhaul(@Nonnull Registry registry) {
        registry.register("storage_overhaul", "storages", () -> new StorageOverhaulRegistry());
        registry.register("storage_overhaul", "barrel", () -> new StorageBarrelRegistry());
        Config.registerConfigsIfModLoaded("storage_overhaul", () -> Utils.arrayOf(
                StorageOverhaulRegistry.CONFIG,
                StorageBarrelRegistry.CONFIG
        ));
    }

    private static class Registry {
        private final IForgeRegistry<ContraptionStorageRegistry> registry;

        public Registry(IForgeRegistry<ContraptionStorageRegistry> registry) {
            this.registry = registry;
        }

        void register(String modId, String registryName, Supplier<ContraptionStorageRegistry> supplier) {
            ContraptionStorageRegistry.registerIfModLoaded(registry, modId, Utils.constructId(modId, registryName), supplier);
        }

        void register(String modId, String registryName, Supplier<ContraptionStorageRegistry> supplier, Supplier<Config.IRegistryInfo> config) {
            register(modId, registryName, supplier);
            Config.registerConfigsIfModLoaded(modId, () -> Utils.arrayOf(config.get()));
        }

        void registerConditionally(Supplier<Boolean> condition, String fullRegistryName, Supplier<ContraptionStorageRegistry> supplier) {
            ContraptionStorageRegistry.registerConditionally(registry, condition, fullRegistryName, supplier);
        }
    }
}
