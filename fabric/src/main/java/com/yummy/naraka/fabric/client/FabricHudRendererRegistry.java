package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.HudRendererRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudLayerRegistrationCallback;
import net.fabricmc.fabric.api.client.rendering.v1.IdentifiedLayer;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricHudRendererRegistry {
    @MethodProxy(HudRendererRegistry.class)
    public static void registerPreLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.attachLayerBefore(IdentifiedLayer.MISC_OVERLAYS, id, factory.get());
        });
    }

    @MethodProxy(HudRendererRegistry.class)
    public static void registerPostLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        HudLayerRegistrationCallback.EVENT.register(layeredDrawer -> {
            layeredDrawer.addLayer(IdentifiedLayer.of(id, factory.get()));
        });
    }
}
