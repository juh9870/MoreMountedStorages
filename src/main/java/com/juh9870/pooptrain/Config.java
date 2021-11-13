package com.juh9870.pooptrain;

import com.juh9870.pooptrain.integrations.enderstorage.EnderStorageRegistry;
import com.juh9870.pooptrain.integrations.immersiveengineering.ImmersiveEngineeringRegistry;
import com.juh9870.pooptrain.integrations.industrialforegoing.IndustrialForegoingRegistry;
import com.juh9870.pooptrain.integrations.ironchests.IronChestsRegistry;
import com.juh9870.pooptrain.integrations.pneumaticcraft.PneumaticcraftRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.CompactingDrawerRegistry;
import com.juh9870.pooptrain.integrations.storagedrawers.StorageDrawersRegistry;
import net.minecraftforge.common.ForgeConfigSpec;

public final class Config {
	public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
	public static final ForgeConfigSpec SPEC;

	static {
		BUILDER.push("Mod integration config");
		{
			EnderStorageRegistry.enabled = BUILDER.comment("Enables Ender Storage integration. Default value is true").define("EnderStorage", true);
			ImmersiveEngineeringRegistry.enabled = BUILDER.comment("Enables Immersive Engineering integration. Default value is true").define("ImmersiveEngineering", true);
			BUILDER.push("Industrial Foregoing");
			{
				IndustrialForegoingRegistry.enabled = BUILDER.comment("Enables Black Hole Unit integration. Default value is true").define("IndustrialForegoingUnit", true);
				IndustrialForegoingRegistry.enabled = BUILDER.comment("Enables Black Hole Controller integration. Default value is true").define("IndustrialForegoingController", true);
			}
			BUILDER.pop();
			IronChestsRegistry.enabled = BUILDER.comment("Enables Iron Chest integration. Default value is true").define("IronChest", true);
			PneumaticcraftRegistry.enabled = BUILDER.comment("Enables Pneumaticcraft integration. Default value is true").define("Pneumaticcraft", true);
			BUILDER.push("Storage Drawers");
			{
				BUILDER.comment("Framed Compacting Drawers integration follow this setting too");
				StorageDrawersRegistry.enabled = BUILDER.comment("Enables standard drawers integration. Default value is true").define("StorageDrawersStandard", true);
				CompactingDrawerRegistry.enabled = BUILDER.comment("Enables compacting drawers integration. Default value is true").define("StorageDrawersCompacting", true);
			}
			BUILDER.pop();
		}
		BUILDER.pop();
		SPEC = BUILDER.build();
	}
}
