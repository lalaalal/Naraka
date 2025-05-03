package com.yummy.naraka.world.inventory;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public class NarakaMenuTypes {
    private static <M extends AbstractContainerMenu> HolderProxy<MenuType<?>, MenuType<M>> register(String name, MenuType<M> type) {
        return RegistryProxy.register(Registries.MENU, name, () -> type);
    }

    public static void initialize() {

    }
}
