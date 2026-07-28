package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ScreenFactoryRegistry;
import com.yummy.naraka.core.registries.ValueGetter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricScreenFactoryRegistry implements ScreenFactoryRegistry.Registrar {
    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(ValueGetter<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> constructor) {
        MenuScreens.register(menu.getConcreteValue(), constructor);
    }
}
