package com.yummy.naraka.client.init;

import com.yummy.naraka.proxy.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class ScreenFactoryRegistry {
    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(Supplier<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> constructor) {
        MethodInvoker.invoke(ScreenFactoryRegistry.class, "register", menu, constructor);
    }
}
