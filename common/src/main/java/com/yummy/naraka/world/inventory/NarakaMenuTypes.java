package com.yummy.naraka.world.inventory;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class NarakaMenuTypes {
    public static final MenuType<SoulCraftingMenu> SOUL_CRAFTING = register(
            "soul_crafting", new MenuType<>(SoulCraftingMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    private static <M extends AbstractContainerMenu> MenuType<M> register(String name, MenuType<M> type) {
        return Registry.register(BuiltInRegistries.MENU, NarakaMod.location(name), type);
    }

    public static void initialize() {

    }
}
