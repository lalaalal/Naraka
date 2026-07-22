package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.ScreenFactoryRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;

import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class NeoForgeScreenFactoryRegistry implements NarakaEventBus, ScreenFactoryRegistry.Registrar {
    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(Supplier<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> factory) {
        NARAKA_BUS.addListener(RegisterMenuScreensEvent.class, event -> {
            event.register(menu.get(), factory);
        });
    }
}
