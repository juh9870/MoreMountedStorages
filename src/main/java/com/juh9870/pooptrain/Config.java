package com.juh9870.pooptrain;

import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	public static final ForgeConfigSpec.ConfigValue<Boolean> ENDER_STORAGE;
	public static final ForgeConfigSpec.ConfigValue<Boolean> IMMERSIVE_ENGINEERING;
	public static final ForgeConfigSpec.ConfigValue<Boolean> INDUSTRIAL_FOREGOING_UNIT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> INDUSTRIAL_FOREGOING_CONTROLLER;
	public static final ForgeConfigSpec.ConfigValue<Boolean> IRON_CHESTS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> PNEUMATICCRAFT;
	public static final ForgeConfigSpec.ConfigValue<Boolean> STORAGE_DRAWERS;
	public static final ForgeConfigSpec.ConfigValue<Boolean> COMPACTING_DRAWER;

	static {
		BUILDER.comment("Mod integration config").push("integration");
		{
			ENDER_STORAGE = BUILDER.comment("Enables Ender Storage integration. Default value is true").define("EnderStorage", true);
			IMMERSIVE_ENGINEERING = BUILDER.comment("Enables Immersive Engineering integration. Default value is true").define("ImmersiveEngineering", true);
			BUILDER.comment("Industrial Foregoing").push("IndustrialForegoing");
			{
				INDUSTRIAL_FOREGOING_UNIT = BUILDER.comment("Enables Black Hole Unit integration. Default value is true").define("BlackHoleUnit", true);
				INDUSTRIAL_FOREGOING_CONTROLLER = BUILDER.comment("Enables Black Hole Controller integration. Default value is true").define("BlackHoleController", true);
			}
			BUILDER.pop();
			IRON_CHESTS = BUILDER.comment("Enables Iron Chest integration. Default value is true").define("IronChest", true);
			PNEUMATICCRAFT = BUILDER.comment("Enables Pneumaticcraft integration. Default value is true").define("Pneumaticcraft", true);
			BUILDER.comment("Storage Drawers").push("StorageDrawers");
			{
				BUILDER.comment("Framed Compacting Drawers integration follow this setting too");
				STORAGE_DRAWERS = BUILDER.comment("Enables standard drawers integration. Default value is true").define("Standard", true);
				COMPACTING_DRAWER = BUILDER.comment("Enables compacting drawers integration. Default value is true").define("Compacting", true);
			}
			BUILDER.pop();
		}
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
