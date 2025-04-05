package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.HudRendererRegistry;
import com.yummy.naraka.proxy.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricHunRendererRegistry {
    @MethodProxy(HudRendererRegistry.class)
    public static void register(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        LayeredDraw.Layer layer = factory.get();
        HudRenderCallback.EVENT.register(layer::render);
    }
}
