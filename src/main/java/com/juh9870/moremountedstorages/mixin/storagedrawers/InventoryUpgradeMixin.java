package com.juh9870.moremountedstorages.mixin.storagedrawers;

import com.jaquadro.minecraft.storagedrawers.block.tile.TileEntityDrawers;
import com.jaquadro.minecraft.storagedrawers.inventory.InventoryUpgrade;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(InventoryUpgrade.class)
public class InventoryUpgradeMixin {
	@Shadow(remap = false)
	private TileEntityDrawers tile;

	@Inject(at = @At("HEAD"), method = "stillValid(Lnet/minecraft/entity/player/PlayerEntity;)Z", remap = false, cancellable = true)
	public void moremountedstorages_stillValid(PlayerEntity player, CallbackInfoReturnable<Boolean> cir) {
		if (tile.getLevel().getBlockEntity(tile.getBlockPos()) != tile ||
				player.distanceToSqr((double) tile.getBlockPos().getX() + 0.5D, (double) tile.getBlockPos().getY() + 0.5D, (double) tile.getBlockPos().getZ() + 0.5D) > 64.0D)
			cir.setReturnValue(false);
	}
}
