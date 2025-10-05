package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.HudRendererRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class NeoForgeHudRendererRegistry implements NarakaEventBus {
    @MethodProxy(HudRendererRegistry.class)
    public static void registerPreLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        NARAKA_BUS.addListener(RegisterGuiLayersEvent.class, event -> {
            event.registerBelowAll(id, factory.get());
        });
    }

    @MethodProxy(HudRendererRegistry.class)
    public static void registerPostLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        NARAKA_BUS.addListener(RegisterGuiLayersEvent.class, event -> {
            event.registerAboveAll(id, factory.get());
        });
    }
}
