package com.yummy.naraka.client.init;

import com.yummy.naraka.client.service.NarakaClientServices;
import com.yummy.naraka.core.registries.ValueGetter;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public abstract class ScreenFactoryRegistry {
    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(ValueGetter<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> constructor) {
        NarakaClientServices.SCREEN_FACTORY_REGISTRY.register(menu, constructor);
    }

    public interface Registrar {
        <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(ValueGetter<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> constructor);
    }
}
