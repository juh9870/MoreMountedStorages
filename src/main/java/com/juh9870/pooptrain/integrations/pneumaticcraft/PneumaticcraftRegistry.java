package com.juh9870.pooptrain.integrations.pneumaticcraft;

import com.juh9870.pooptrain.ContraptionItemStackHandler;
import com.juh9870.pooptrain.ContraptionStorageRegistry;
import com.juh9870.pooptrain.helpers.WrapperStackHandler;
import me.desht.pneumaticcraft.common.core.ModTileEntities;
import me.desht.pneumaticcraft.common.tileentity.TileEntitySmartChest;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.registries.IForgeRegistry;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PneumaticcraftRegistry extends ContraptionStorageRegistry {
	private static final Pattern buildIdPattern = Pattern.compile("(\\d+)$");
	public static final Lazy<ContraptionStorageRegistry> INSTANCE = createConditionally(
			() -> {
				Optional<? extends ModContainer> container = ModList.get().getModContainerById("pneumaticcraft");
				if (container.isPresent()) {
					String versionCode = container.get().getModInfo().getVersion().getQualifier();
					Matcher matcher = buildIdPattern.matcher(versionCode);
					if (!matcher.find()) return false;
					int buildId = Integer.parseInt(matcher.group(1));
					return buildId >= 273;
				}
				return false;
			},
			"pneumaticcraft:smart_chest",
			PneumaticcraftRegistry::new
	);
	public static ForgeConfigSpec.ConfigValue<Boolean> enabled;

	public static void register(IForgeRegistry<ContraptionStorageRegistry> registry) {
		registry.register(INSTANCE.get());
	}

	@Override
	public TileEntityType<?>[] affectedStorages() {
		return new TileEntityType[]{
				ModTileEntities.SMART_CHEST.get()
		};
	}

	@Override
	public boolean canUseAsStorage(TileEntity te) {
		return getHandlerFromDefaultCapability(te) instanceof TileEntitySmartChest.SmartChestItemHandler && enabled.get();
	}

	@Override
	public ContraptionItemStackHandler createHandler(TileEntity te) {

		IItemHandler handler = getHandlerFromDefaultCapability(te);
		if (handler == dummyHandler) {
			return null;
		}
		if (!(handler instanceof TileEntitySmartChest.SmartChestItemHandler)) {
			return null;
		}

		return new SmartChestWrapper((TileEntitySmartChest.SmartChestItemHandler) handler);
	}

	@Override
	public ContraptionItemStackHandler deserializeHandler(CompoundNBT nbt) {
		return new SmartChestWrapper((ItemStackHandler) TileEntitySmartChest.deserializeSmartChest(nbt));
	}

	public static class SmartChestWrapper extends WrapperStackHandler {

		public SmartChestWrapper(ItemStackHandler handler) {
			super(handler);
		}

		@Override
		protected ContraptionStorageRegistry registry() {
			return INSTANCE.get();
		}
	}
}
