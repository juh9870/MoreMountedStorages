package com.example.examplemod.mixin;

import com.example.examplemod.EnderStackHandler;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import net.minecraftforge.items.wrapper.CombinedInvWrapper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Contraption.ContraptionInvWrapper.class)
public class ContraptionInvWrapperMixin extends CombinedInvWrapper {

	@Inject(at = @At(value = "INVOKE_ASSIGN", target = "Lcom/simibubi/create/content/contraptions/components/structureMovement/Contraption$ContraptionInvWrapper;getHandlerFromIndex(I)Lnet/minecraftforge/items/IItemHandlerModifiable;"), method = "Lcom/simibubi/create/content/contraptions/components/structureMovement/Contraption$ContraptionInvWrapper;isSlotExternal(I)Z", remap = false, cancellable = true)
	public void examplemod_isSlotExternal(int slot, CallbackInfoReturnable<Boolean> cir) {
		if (this.getHandlerFromIndex(this.getIndexForSlot(slot)) instanceof EnderStackHandler) {
			cir.setReturnValue(true);
		}
	}
}
