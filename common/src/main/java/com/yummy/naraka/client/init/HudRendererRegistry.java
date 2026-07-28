package com.yummy.naraka.client.init;

import com.yummy.naraka.client.gui.hud.HudRenderer;
import com.yummy.naraka.client.service.NarakaClientServices;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class HudRendererRegistry {
    public static void registerPreLayer(ResourceLocation id, Supplier<HudRenderer> layer) {
        NarakaClientServices.HUD_RENDERER_REGISTRY.registerPreLayer(id, layer);
    }

    public static void registerPostLayer(ResourceLocation id, Supplier<HudRenderer> factory) {
        NarakaClientServices.HUD_RENDERER_REGISTRY.registerPostLayer(id, factory);
    }

    public interface Registrar {
        void registerPreLayer(ResourceLocation id, Supplier<HudRenderer> factory);

        void registerPostLayer(ResourceLocation id, Supplier<HudRenderer> factory);
    }
}
