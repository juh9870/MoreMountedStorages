package com.juh9870.moremountedstorages.mixin.dimstorage;

import com.juh9870.moremountedstorages.integrations.dimstorage.DimStorageRegistry;
import edivad.dimstorage.manager.DimStorageManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DimStorageManager.class)
public class DimStorageManagerMixin {

	@Inject(at = @At("HEAD"), method = "reloadManager(Z)V", remap = false)
	private static void moremountedstorages_reloadManager(boolean client, CallbackInfo ci) {
		DimStorageRegistry.managerGeneration++;
	}
}
