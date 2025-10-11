package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.gui.hud.HudRenderer;
import com.yummy.naraka.client.init.HudRendererRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricHudRendererRegistry {
    @MethodProxy(HudRendererRegistry.class)
    public static void registerPreLayer(ResourceLocation id, Supplier<HudRenderer> factory) {
        HudElementRegistry.attachElementBefore(VanillaHudElements.MISC_OVERLAYS, id, factory.get()::render);
    }

    @MethodProxy(HudRendererRegistry.class)
    public static void registerPostLayer(ResourceLocation id, Supplier<HudRenderer> factory) {
        HudElementRegistry.addLast(id, factory.get()::render);
    }
}
