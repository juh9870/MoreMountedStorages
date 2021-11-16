package com.juh9870.moremountedstorages.mixin.enderchests;

import com.juh9870.moremountedstorages.integrations.enderchests.EnderChestsRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import shetiphian.enderchests.common.misc.ChestHelper;

@Mixin(ChestHelper.class)
public class ChestHelperMixin {
	@Inject(at = @At("HEAD"), method = "loadChestData", remap = false)
	public void moremountedstorages_loadChestData(CallbackInfo ci) {
		EnderChestsRegistry.managerGeneration++;
	}
}
