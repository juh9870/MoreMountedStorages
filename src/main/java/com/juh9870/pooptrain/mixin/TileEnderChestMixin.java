package com.juh9870.pooptrain.mixin;

import codechicken.enderstorage.storage.EnderItemStorage;
import codechicken.enderstorage.tile.TileEnderChest;
import com.juh9870.pooptrain.EnderStackHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TileEnderChest.class)
public class TileEnderChestMixin {
	@Shadow(remap = false)
	private LazyOptional<IItemHandler> itemHandler = LazyOptional.empty();

	@Inject(at=@At("RETURN"), method = "onFrequencySet()V", remap = false)
	public void pooptrain_onFrequencySet(CallbackInfo callback) {
		itemHandler=LazyOptional.of(() -> new EnderStackHandler(getStorage()));
	}

	@Shadow(remap = false)
	public EnderItemStorage getStorage() {
		throw new IllegalStateException("pooptrain mixin failed to shadow getStorage()");
	}
}
