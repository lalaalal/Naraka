package com.yummy.naraka.world.inventory;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaMenuTypes {
    private static final DeferredRegister<MenuType<?>> MENU_TYPES = DeferredRegister.create(Registries.MENU, NarakaMod.MOD_ID);

    public static final DeferredHolder<MenuType<?>, MenuType<SoulCraftingMenu>> SOUL_CRAFTING = MENU_TYPES.register(
            "soul_crafting",
            () -> new MenuType<>(SoulCraftingMenu::new, FeatureFlags.DEFAULT_FLAGS)
    );

    public static void register(IEventBus bus) {
        MENU_TYPES.register(bus);
    }
}
