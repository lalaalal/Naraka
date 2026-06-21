package com.yummy.naraka.client.init;

import com.yummy.naraka.client.NarakaClientServices;
import com.yummy.naraka.client.gui.hud.HudRenderer;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public abstract class HudRendererRegistry {
    public static void registerPreLayer(Identifier id, Supplier<HudRenderer> layer) {
        NarakaClientServices.HUD_RENDERER_REGISTRY.registerPreLayer(id, layer);
    }

    public static void registerPostLayer(Identifier id, Supplier<HudRenderer> factory) {
        NarakaClientServices.HUD_RENDERER_REGISTRY.registerPostLayer(id, factory);
    }

    public interface Registrar {
        void registerPreLayer(Identifier id, Supplier<HudRenderer> layer);

        void registerPostLayer(Identifier id, Supplier<HudRenderer> factory);
    }
}
