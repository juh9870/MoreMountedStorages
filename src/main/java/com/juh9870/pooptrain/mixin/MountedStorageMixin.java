package com.juh9870.pooptrain.mixin;

import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.simibubi.create.content.contraptions.components.structureMovement.MountedStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MountedStorage.class)
public class MountedStorageMixin {

	@Shadow(remap = false)
	@Final
	private static ItemStackHandler dummyHandler;
	@Shadow(remap = false)
	ItemStackHandler handler;
	@Shadow(remap = false)
	boolean valid;
	@Shadow(remap = false)
	private TileEntity te;

	public MountedStorageMixin(TileEntity te) {
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;"), method = "canUseAsStorage(Lnet/minecraft/tileentity/TileEntity;)Z", remap = false, cancellable = true)
	private static void pooptrain_canUseAsStorage(TileEntity te, CallbackInfoReturnable<Boolean> cir) {
		if (ContraptionStorageRegistry.canUseAsStorage(te)) cir.setReturnValue(true);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;deserializeNBT(Lnet/minecraft/nbt/CompoundNBT;)V"), method = "deserialize(Lnet/minecraft/nbt/CompoundNBT;)Lcom/simibubi/create/content/contraptions/components/structureMovement/MountedStorage;", remap = false, cancellable = true)
	private static void pooptrain_deserializeMountedStorage(CompoundNBT nbt, CallbackInfoReturnable<MountedStorageMixin> cir) {
		if (nbt.contains(ContraptionStorageRegistry.CLASS_NAME)) {
			MountedStorageMixin storage = new MountedStorageMixin(null);
			try {
				Class<?> cl = Class.forName(nbt.getString(ContraptionStorageRegistry.CLASS_NAME));
				storage.handler = ContraptionStorageRegistry.getStorage(cl).deserializeHandler(nbt);
				storage.valid = true;
			} catch (ClassNotFoundException e) {
				storage.valid = false;
			}
			cir.setReturnValue(storage);
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;"), method = "removeStorageFromWorld()V", remap = false, cancellable = true)
	private void pooptrain_removeStorageFromWorld(CallbackInfo ci) {
		ContraptionStorageRegistry storage = ContraptionStorageRegistry.getStorage(te);
		if (storage == null) return;
		handler = dummyHandler;
		if (!storage.prepareStorageForContraption(te)) {
			return;
		}
		if (storage.useCustomHandler()) {
			handler = storage.getHandler(te);
			if (handler != null) {
				valid = true;
			}
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "addStorageToWorld(Lnet/minecraft/tileentity/TileEntity;)V", remap = false, cancellable = true)
	private void pooptrain_addStorageToWorld(TileEntity te, CallbackInfo ci) {
		ContraptionStorageRegistry storage = ContraptionStorageRegistry.getStorage(te);
		if (storage == null) return;
		if (storage.addStorageToWorld(te, handler)) ci.cancel();
	}
}
