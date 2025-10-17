package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class HudRendererRegistry {
    public static void registerPreLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> layer) {
        MethodInvoker.of(HudRendererRegistry.class, "registerPreLayer")
                .invoke(id, layer);
    }

    public static void registerPostLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        MethodInvoker.of(HudRendererRegistry.class, "registerPostLayer")
                .invoke(id, factory);
    }
}
