package com.juh9870.pooptrain.mixin;

import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.MountedStorage;
import com.simibubi.create.content.contraptions.components.structureMovement.StructureTransform;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Map;

@Mixin(Contraption.class)
public class ContraptionMixin {

	@Shadow(remap = false)
	protected Map<BlockPos, MountedStorage> storage;

	@Inject(at = @At(value = "NEW", target = "Lcom/simibubi/create/content/contraptions/components/structureMovement/Contraption$ContraptionInvWrapper;<init>([Lnet/minecraftforge/items/IItemHandlerModifiable;)V"), method = "readNBT(Lnet/minecraft/world/World;Lnet/minecraft/nbt/CompoundNBT;Z)V", remap = false)
	public void readNBT(World world, CompoundNBT nbt, boolean spawnData, CallbackInfo cbi) {
		for (MountedStorage value : this.storage.values()) {
			IItemHandlerModifiable handler = value.getItemHandler();
			if (handler instanceof ContraptionStorageRegistry.IWorldRequiringHandler) {
				((ContraptionStorageRegistry.IWorldRequiringHandler) handler).applyWorldOnDeserialization(world);
			}
		}
	}


	@Inject(at = @At("TAIL"), method = "addBlocksToWorld(Lnet/minecraft/world/World;Lcom/simibubi/create/content/contraptions/components/structureMovement/StructureTransform;)V", remap = false)
	public void addBlocksToWorld(World FluidState, StructureTransform mountedStorage, CallbackInfo ci) {
		for (MountedStorage value : storage.values()) {
			IItemHandlerModifiable handler = value.getItemHandler();
			if(handler instanceof ContraptionStorageRegistry.IAfterStoragePlacedHandler){
				((ContraptionStorageRegistry.IAfterStoragePlacedHandler) handler).afterAddStorageToWorld();
			}
		}
	}
}
