package com.juh9870.pooptrain.mixin;

import com.juh9870.pooptrain.ContraptionItemStackHandler;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.juh9870.pooptrain.PoopTrain;
import com.simibubi.create.content.contraptions.components.structureMovement.MountedStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
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
		ContraptionStorageRegistry handler = ContraptionStorageRegistry.forTileEntity(te.getType());
		if (handler != null && handler.canUseAsStorage(te)) cir.setReturnValue(true);
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;deserializeNBT(Lnet/minecraft/nbt/CompoundNBT;)V"), method = "deserialize(Lnet/minecraft/nbt/CompoundNBT;)Lcom/simibubi/create/content/contraptions/components/structureMovement/MountedStorage;", remap = false, cancellable = true)
	private static void pooptrain_deserializeMountedStorage(CompoundNBT nbt, CallbackInfoReturnable<MountedStorageMixin> cir) {
		PoopTrain.breakpoint();
		if (nbt.contains(ContraptionStorageRegistry.REGISTRY_NAME)) {
			MountedStorageMixin storage = new MountedStorageMixin(null);
			String id = nbt.getString(ContraptionStorageRegistry.REGISTRY_NAME);
			ContraptionStorageRegistry registry = ContraptionStorageRegistry.REGISTRY.get().getValue(new ResourceLocation(id));
			if (registry == null) return;
			storage.handler = registry.deserializeHandler(nbt);
			if (storage.handler == null) storage.handler = dummyHandler;
			else storage.valid = true;
			cir.setReturnValue(storage);
		}
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraft/tileentity/TileEntity;getCapability(Lnet/minecraftforge/common/capabilities/Capability;)Lnet/minecraftforge/common/util/LazyOptional;"), method = "removeStorageFromWorld()V", remap = false, cancellable = true)
	private void pooptrain_removeStorageFromWorld(CallbackInfo ci) {
		ContraptionStorageRegistry storage = ContraptionStorageRegistry.forTileEntity(te.getType());
		if (storage == null) return;
		handler = dummyHandler;
		handler = storage.createHandler(te);
		if (handler != null) {
			valid = true;
			if (handler instanceof ContraptionStorageRegistry.IWorldRequiringHandler) {
				((ContraptionStorageRegistry.IWorldRequiringHandler) handler).applyWorld(te.getLevel());
			}
			ci.cancel();
		}
	}

	@Inject(at = @At("HEAD"), method = "addStorageToWorld(Lnet/minecraft/tileentity/TileEntity;)V", remap = false, cancellable = true)
	private void pooptrain_addStorageToWorld(TileEntity te, CallbackInfo ci) {
		if (handler instanceof ContraptionItemStackHandler) {
			boolean cancel = !((ContraptionItemStackHandler) handler).addStorageToWorld(te);
			if (cancel) {
				ci.cancel();
			}
		}
	}
}
