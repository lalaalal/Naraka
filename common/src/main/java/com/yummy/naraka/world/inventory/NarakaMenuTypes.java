package com.yummy.naraka.world.inventory;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class NarakaMenuTypes {
    public static final LazyHolder<MenuType<?>, MenuType<SoulCraftingMenu>> SOUL_CRAFTING = register(
            "soul_crafting", new MenuType<>(SoulCraftingMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private static <M extends AbstractContainerMenu> LazyHolder<MenuType<?>, MenuType<M>> register(String name, MenuType<M> type) {
        return RegistryProxy.register(Registries.MENU, name, () -> type);
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.MENU)
                .onRegistrationFinished();
    }
}
