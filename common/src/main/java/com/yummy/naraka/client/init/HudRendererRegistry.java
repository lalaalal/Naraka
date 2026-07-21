package com.yummy.naraka.client.init;

import com.yummy.naraka.client.gui.hud.HudRenderer;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public abstract class HudRendererRegistry {
    public static void registerPreLayer(ResourceLocation id, Supplier<HudRenderer> layer) {
        MethodInvoker.of(HudRendererRegistry.class, "registerPreLayer")
                .invoke(id, layer);
    }

    public static void registerPostLayer(ResourceLocation id, Supplier<HudRenderer> factory) {
        MethodInvoker.of(HudRendererRegistry.class, "registerPostLayer")
                .invoke(id, factory);
    }
}
