package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.ScreenFactoryRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ForgeScreenFactoryRegistry implements NarakaEventBus {
    @MethodProxy(ScreenFactoryRegistry.class)
    public static <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(Supplier<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> factory) {

    }
}
