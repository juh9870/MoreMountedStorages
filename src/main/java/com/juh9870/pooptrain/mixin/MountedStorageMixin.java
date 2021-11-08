package com.juh9870.pooptrain.mixin;

import com.juh9870.pooptrain.EnderStackHandler;
import com.simibubi.create.content.contraptions.components.structureMovement.MountedStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(MountedStorage.class)
public class MountedStorageMixin {

	@Shadow(remap = false)
	ItemStackHandler handler;
	@Shadow(remap = false)
	boolean valid;
	public MountedStorageMixin(TileEntity te) {
	}

	@Inject(at = @At(value = "INVOKE", target = "Lnet/minecraftforge/items/ItemStackHandler;deserializeNBT(Lnet/minecraft/nbt/CompoundNBT;)V"), method = "Lcom/simibubi/create/content/contraptions/components/structureMovement/MountedStorage;deserialize(Lnet/minecraft/nbt/CompoundNBT;)Lcom/simibubi/create/content/contraptions/components/structureMovement/MountedStorage;", remap = false, cancellable = true)
	private static void pooptrain_deserializeMountedStorage(CompoundNBT nbt, CallbackInfoReturnable<MountedStorageMixin> cir) {
		if (nbt.contains("Frequency")) {
			MountedStorageMixin storage = new MountedStorageMixin(null);
			storage.handler = new EnderStackHandler();
			storage.handler.deserializeNBT(nbt);
			storage.valid = true;
			cir.setReturnValue(storage);
		}
	}
}
