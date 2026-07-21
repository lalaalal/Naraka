package com.yummy.naraka.client.init;

import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

public abstract class ScreenFactoryRegistry {
    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(ValueGetter<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> constructor) {
        MethodInvoker.invoke(ScreenFactoryRegistry.class, "register", menu, constructor);
    }
}
