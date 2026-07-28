package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.ScreenFactoryRegistry;
import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ForgeScreenFactoryRegistry implements ScreenFactoryRegistry.Registrar, NarakaEventBus {
    @Override
    public <M extends AbstractContainerMenu, S extends AbstractContainerScreen<M>> void register(ValueGetter<MenuType<M>> menu, MenuScreens.ScreenConstructor<M, S> factory) {

    }
}
