package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ScreenFactoryRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricScreenFactoryRegistry {
    @MethodProxy(ScreenFactoryRegistry.class)
    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(Supplier<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> constructor) {
        MenuScreens.register(menu.get(), constructor);
    }
}
