package com.yummy.naraka.world.inventory;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class NarakaMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.MENU);

    public static final RegistrySupplier<MenuType<SoulCraftingMenu>> SOUL_CRAFTING = register(
            "soul_crafting", new MenuType<>(SoulCraftingMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private static <M extends AbstractContainerMenu> RegistrySupplier<MenuType<M>> register(String name, MenuType<M> type) {
        return MENU_TYPES.register(name, () -> type);
    }

    public static void initialize() {
        MENU_TYPES.register();
    }
}
