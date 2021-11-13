package com.juh9870.moremountedstorages.mixin.enderstorage;

import codechicken.enderstorage.manager.EnderStorageManager;
import com.juh9870.moremountedstorages.integrations.enderstorage.EnderStorageRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(EnderStorageManager.class)
public class EnderStorageManagerMixin {

	@Inject(at = @At("HEAD"), method = "reloadManager(Z)V", remap = false)
	private static void moremountedstorages_reloadManager(boolean client, CallbackInfo ci) {
		EnderStorageRegistry.managerGeneration++;
	}
}
