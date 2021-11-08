package com.example.examplemod.mixin;

import codechicken.enderstorage.api.Frequency;
import codechicken.enderstorage.storage.EnderItemStorage;
import codechicken.enderstorage.tile.TileEnderChest;
import com.example.examplemod.EnderStackHandler;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.wrapper.InvWrapper;
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
	public void examplemod_onFrequencySet(CallbackInfo callback) {
		itemHandler=LazyOptional.of(() -> new EnderStackHandler(getStorage()));
	}

	@Shadow(remap = false)
	public EnderItemStorage getStorage() {
		throw new IllegalStateException("examplemod mixin failed to shadow getStorage()");
	}
}
