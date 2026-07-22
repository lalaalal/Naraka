package com.yummy.naraka.client.init;

import com.yummy.naraka.client.service.NarakaClientServices;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class HudRendererRegistry {
    public static void registerPreLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> layer) {
        NarakaClientServices.HUD_RENDERER_REGISTRY.registerPreLayer(id, layer);
    }

    public static void registerPostLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        NarakaClientServices.HUD_RENDERER_REGISTRY.registerPostLayer(id, factory);
    }

    public interface Registrar {
        void registerPreLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory);

        void registerPostLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory);
    }
}
