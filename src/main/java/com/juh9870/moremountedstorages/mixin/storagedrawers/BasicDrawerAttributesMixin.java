package com.juh9870.moremountedstorages.mixin.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.api.storage.attribute.LockAttribute;
import com.jaquadro.minecraft.storagedrawers.capabilities.BasicDrawerAttributes;
import net.minecraft.nbt.CompoundNBT;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;

@Mixin(BasicDrawerAttributes.class)
public class BasicDrawerAttributesMixin {

	@Shadow(remap = false)
	private EnumSet<LockAttribute> itemLock;

	@Inject(at = @At("TAIL"), method = "deserializeNBT(Lnet/minecraft/nbt/CompoundNBT;)V", remap = false)
	public void moremountedstorages_deserializeNBT(CompoundNBT nbt, CallbackInfo ci) {
		if (itemLock == null) itemLock = EnumSet.noneOf(LockAttribute.class);
	}
}