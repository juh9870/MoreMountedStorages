package com.juh9870.moremountedstorages;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;

import static com.juh9870.moremountedstorages.ContraptionItemStackHandler.PRIORITY_ITEM_BIN;

public final class Config {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final RegistryInfo ENDER_STORAGE = new RegistryInfo("enderstorage", "ender_chest", "Ender Storage", 1);
	public static final RegistryInfo IMMERSIVE_ENGINEERING = new RegistryInfo("immersiveengineering", "crate", "Immersive Engineering Crate");
	public static final RegistryInfo INDUSTRIAL_FOREGOING_UNIT = new RegistryInfo("industrialforegoing", "black_hole_unit", "Black Hole Unit", PRIORITY_ITEM_BIN);
	public static final RegistryInfo INDUSTRIAL_FOREGOING_CONTROLLER = new RegistryInfo("industrialforegoing", "black_hole_controller", "Black Hole Controller", PRIORITY_ITEM_BIN);
	public static final RegistryInfo IRON_CHESTS = new RegistryInfo("ironchest", "chest", "Iron Chests");
	public static final RegistryInfo PNEUMATICCRAFT = new RegistryInfo("pneumaticcraft", "smart_chest", "PneumaticCraft Smart Chest", 1);
	public static final RegistryInfo STORAGE_DRAWERS = new RegistryInfo("storagedrawers", "drawer", "standard drawers", PRIORITY_ITEM_BIN);
	public static final RegistryInfo COMPACTING_DRAWER = new RegistryInfo("storagedrawers", "compacting_drawer", "compacting drawers", PRIORITY_ITEM_BIN);
	public static final RegistryInfo EXPANDED_STORAGE = new RegistryInfo("expandedstorage", "chest", "Expanded Storage");

	static {
		BUILDER.comment("Mod integration config").push("Integration");
		{
			ENDER_STORAGE.register();
			IMMERSIVE_ENGINEERING.register();
			new ModRegistry("industrialforegoing", new RegistryInfo[]{
					INDUSTRIAL_FOREGOING_UNIT,
					INDUSTRIAL_FOREGOING_CONTROLLER
			}).register();
			IRON_CHESTS.register();
			PNEUMATICCRAFT.register();
			new ModRegistry("storagedrawers", new RegistryInfo[]{
					STORAGE_DRAWERS,
					COMPACTING_DRAWER
			}).register();
			EXPANDED_STORAGE.register();
		}
		BUILDER.pop();
		SPEC = BUILDER.build();
	}

	public static class RegistryInfo {
		private final String modId;
		private final String id;
		private final String name;
		private final int defaultPriority;
		private final boolean usePriority;
		private ForgeConfigSpec.ConfigValue<Boolean> enabled = null;
		private ForgeConfigSpec.ConfigValue<Integer> priority = null;

		public RegistryInfo(String modId, String id, String name, int defaultPriority) {
			this.modId = modId;
			this.id = id;
			this.name = name;
			this.defaultPriority = defaultPriority;
			this.usePriority = true;
		}

		public RegistryInfo(String modId, String id, String name) {
			this.modId = modId;
			this.id = id;
			this.name = name;
			this.defaultPriority = 0;
			this.usePriority = false;
		}

		public Boolean isEnabled() {
			return enabled.get();
		}

		public Integer getPriority() {
			return priority.get();
		}

		public void register() {
			if (!ModList.get().isLoaded(modId)) return;
			BUILDER.comment(name).push(id);
			enabled = BUILDER.comment("Enabled " + name + " integration. Default value is true").define("enabled", true);
			if (usePriority)
				priority = BUILDER.comment(name + " storage priority. Items are inserted first into storages with higher priority. Default value is " + defaultPriority).define("priority", defaultPriority);
			BUILDER.pop();
		}
	}

	public static class ModRegistry {
		private final String modId;
		private final RegistryInfo[] registries;

		public ModRegistry(String modId, RegistryInfo[] registries) {
			this.modId = modId;
			this.registries = registries;
		}

		public void register() {
			if (!ModList.get().isLoaded(modId)) return;
			String name = ModList.get().getModContainerById(modId).get().getModInfo().getDisplayName();
			BUILDER.comment(name).push(modId);
			for (RegistryInfo registry : registries) {
				registry.register();
			}
			BUILDER.pop();
		}
	}
}
