package com.juh9870.moremountedstorages;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModList;

import java.util.function.Supplier;

public final class Config {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static ForgeConfigSpec SPEC;

	static void registerConfigIfModLoaded(String modid, Supplier<IRegistryInfo> registry) {
		if (!ModList.get().isLoaded(modid)) return;
		registry.get().register(BUILDER);
	}

	static void registerConfigsIfModLoaded(String modid, Supplier<IRegistryInfo[]> registries) {
		if (!ModList.get().isLoaded(modid)) return;
		new ModRegistry(modid, registries.get()).register(BUILDER);
	}

	interface IRegistryInfo {
		void register(ForgeConfigSpec.Builder builder);
	}

	public static class RegistryInfo implements IRegistryInfo {
		protected final String id;
		protected final String name;
		private ForgeConfigSpec.ConfigValue<Boolean> enabled = null;

		public RegistryInfo(String id, String name) {
			this.id = id;
			this.name = name;
		}

		public Boolean isEnabled() {
			return enabled != null ? enabled.get() : false;
		}

		protected void registerEntries(ForgeConfigSpec.Builder builder) {
			enabled = builder.comment("Enabled " + name + " integration. Default value is true").define("enabled", true);
		}

		public final void register(ForgeConfigSpec.Builder builder) {
			BUILDER.comment(name).push(id);
			registerEntries(builder);
			BUILDER.pop();
		}
	}

	public static class PriorityRegistryInfo extends RegistryInfo {
		private final int defaultPriority;
		private ForgeConfigSpec.ConfigValue<Integer> priority = null;

		public PriorityRegistryInfo(String id, String name, int defaultPriority) {
			super(id, name);
			this.defaultPriority = defaultPriority;
		}

		@Override
		protected void registerEntries(ForgeConfigSpec.Builder builder) {
			super.registerEntries(builder);
			priority = BUILDER.comment(name + " storage priority. Items are inserted first into storages with higher priority. Default value is " + defaultPriority).define("priority", defaultPriority);
		}

		public Integer getPriority() {
			return priority != null ? priority.get() : -1;
		}
	}

	public static class ModRegistry implements IRegistryInfo {
		private final String modId;
		private final IRegistryInfo[] registries;

		public ModRegistry(String modId, IRegistryInfo[] registries) {
			this.modId = modId;
			this.registries = registries;
		}

		public void register(ForgeConfigSpec.Builder builder) {
			String name = ModList.get().getModContainerById(modId).get().getModInfo().getDisplayName();
			BUILDER.comment(name).push(modId);
			for (IRegistryInfo registry : registries) {
				registry.register(builder);
			}
			BUILDER.pop();
		}
	}
}
