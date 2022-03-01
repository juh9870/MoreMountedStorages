package com.juh9870.moremountedstorages.mixin;

import com.juh9870.moremountedstorages.ContraptionItemStackHandler;
import com.juh9870.moremountedstorages.ContraptionStorageRegistry;
import com.simibubi.create.content.contraptions.components.crafter.MechanicalCrafterTileEntity;
import com.simibubi.create.content.contraptions.components.structureMovement.MountedStorage;
import com.simibubi.create.content.contraptions.processing.ProcessingInventory;
import com.simibubi.create.content.logistics.block.inventories.BottomlessItemHandler;
import com.simibubi.create.foundation.utility.NBTHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.ItemStackHandler;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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

	/**
	 * @author juh9870
	 */
	@Overwrite(remap = false)
	public static boolean canUseAsStorage(TileEntity te) {
		if (te == null)
			return false;

		if (te instanceof MechanicalCrafterTileEntity)
			return false;

		ContraptionStorageRegistry registry = ContraptionStorageRegistry.forTileEntity(te.getType());
		if (registry != null) return registry.canUseAsStorage(te);

		LazyOptional<IItemHandler> capability = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY);
		IItemHandler handler = capability.orElse(null);
		return handler instanceof ItemStackHandler && !(handler instanceof ProcessingInventory);
	}

	@Inject(at = @At(value = "HEAD"), method = "deserialize(Lnet/minecraft/nbt/CompoundNBT;)Lcom/simibubi/create/content/contraptions/components/structureMovement/MountedStorage;", remap = false, cancellable = true)
	private static void moremountedstorages_deserializeMountedStorage(CompoundNBT nbt, CallbackInfoReturnable<MountedStorageMixin> cir) {
		MountedStorageMixin storage = new MountedStorageMixin(null);
		storage.handler = new ItemStackHandler();
		if (nbt == null){
			cir.setReturnValue(storage);
			return;
		}

		if (nbt.contains(ContraptionStorageRegistry.REGISTRY_NAME)) {
			String id = nbt.getString(ContraptionStorageRegistry.REGISTRY_NAME);
			ContraptionStorageRegistry registry = ContraptionStorageRegistry.REGISTRY.get().getValue(new ResourceLocation(id));
			if (registry != null) {
				storage.handler = registry.deserializeHandler(nbt);
				if (storage.handler == null) {
					storage.handler = dummyHandler;
				} else {
					storage.valid = true;
				}
				cir.setReturnValue(storage);
			}
		}

		storage.valid = true;

		storage.handler.deserializeNBT(nbt);
		cir.setReturnValue(storage);
	}

	/**
	 * @author juh9870
	 */
	@Overwrite(remap = false)
	public void removeStorageFromWorld() {
		valid = false;
		if (te == null)
			return;

		ContraptionStorageRegistry registry = ContraptionStorageRegistry.forTileEntity(te.getType());
		if (registry == null) return;
		IItemHandler teHandler = registry.createHandler(te);
		if (teHandler != null) {
			handler = (ContraptionItemStackHandler) teHandler;
			valid = true;
			return;
		}

		teHandler = te.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY)
				.orElse(dummyHandler);
		if (teHandler == dummyHandler)
			return;

		// te uses ItemStackHandler
		if (teHandler instanceof ItemStackHandler) {
			handler = (ItemStackHandler) teHandler;
			valid = true;
			return;
		}

		// serialization not accessible -> fill into a serializable handler
		if (teHandler instanceof IItemHandlerModifiable) {
			IItemHandlerModifiable inv = (IItemHandlerModifiable) teHandler;
			handler = new ItemStackHandler(teHandler.getSlots());
			for (int slot = 0; slot < handler.getSlots(); slot++) {
				handler.setStackInSlot(slot, inv.getStackInSlot(slot));
				inv.setStackInSlot(slot, ItemStack.EMPTY);
			}
			valid = true;
			return;
		}
	}

	/**
	 * @author juh9870
	 */
	@Overwrite(remap = false)
	public CompoundNBT serialize() {
		if (!valid)
			return null;
		return handler.serializeNBT();
	}

	@Inject(at = @At("HEAD"), method = "addStorageToWorld(Lnet/minecraft/tileentity/TileEntity;)V", remap = false, cancellable = true)
	private void moremountedstorages_addStorageToWorld(TileEntity te, CallbackInfo ci) {
		if (handler instanceof ContraptionItemStackHandler) {
			boolean cancel = !((ContraptionItemStackHandler) handler).addStorageToWorld(te);
			if (cancel) {
				ci.cancel();
			}
		}
	}
}
