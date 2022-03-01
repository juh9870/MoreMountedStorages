package com.juh9870.moremountedstorages.mixin;

import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.juh9870.moremountedstorages.MoreMountedStorages;
import com.simibubi.create.content.contraptions.components.structureMovement.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.Contraption;
import com.simibubi.create.content.contraptions.components.structureMovement.MountedStorage;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Mixin(Contraption.class)
public class ContraptionMixin {

	@Shadow(remap = false)
	public Contraption.ContraptionInvWrapper inventory;
	@Shadow(remap = false)
	protected Map<BlockPos, MountedStorage> storage;

	@Inject(at = @At(value = "INVOKE", target = "Ljava/util/Map;clear()V", ordinal = 5), method = "readNBT", remap = false)
	public void moremountedstorages__readNBT(World world, CompoundNBT nbt, boolean spawnData, CallbackInfo cbi) {
		MoreMountedStorages.breakpoint();
		for (MountedStorage value : this.storage.values()) {
			IItemHandlerModifiable handler = value.getItemHandler();
			if (handler instanceof ContraptionItemStackHandler) {
				((ContraptionItemStackHandler) handler).applyWorld(world);
			}
		}
	}

	@Inject(at = @At("TAIL"), method = "onEntityCreated", remap = false)
	public void moremountedstorages__onEntityCreated(AbstractContraptionEntity face, CallbackInfo ci) {

		// Gather item handlers of mounted storage
		List<IItemHandlerModifiable> list = storage.values()
				.stream()
				.map(MountedStorage::getItemHandler).sorted((a, b) -> {
					int priorityA = 0;
					int priorityB = 0;
					if (a instanceof ContraptionItemStackHandler)
						priorityA = ((ContraptionItemStackHandler) a).getPriority();
					if (b instanceof ContraptionItemStackHandler)
						priorityB = ((ContraptionItemStackHandler) b).getPriority();
					return -(priorityA - priorityB);
				}).collect(Collectors.toList());
		inventory =
				new Contraption.ContraptionInvWrapper(Arrays.copyOf(list.toArray(), list.size(), IItemHandlerModifiable[].class));
	}
}
