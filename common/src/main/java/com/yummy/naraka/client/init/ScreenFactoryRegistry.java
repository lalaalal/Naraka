package com.yummy.naraka.client.init;

import com.yummy.naraka.client.NarakaClientServices;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

public abstract class ScreenFactoryRegistry {
    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(Supplier<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> constructor) {
        NarakaClientServices.SCREEN_FACTORY_REGISTRY.register(menu, constructor);
    }

    public interface Registrar {
        <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(Supplier<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> constructor);
    }
}
