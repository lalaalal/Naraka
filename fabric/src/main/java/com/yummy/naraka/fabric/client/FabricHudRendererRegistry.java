package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.gui.hud.HudRenderer;
import com.yummy.naraka.client.init.HudRendererRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class FabricHudRendererRegistry implements HudRendererRegistry.Registrar {
    @Override
    public void registerPreLayer(ResourceLocation id, Supplier<HudRenderer> factory) {
        HudRenderCallback.EVENT.register(factory.get()::render);
    }

    @Override
    public void registerPostLayer(ResourceLocation id, Supplier<HudRenderer> factory) {
        HudRenderCallback.EVENT.register(factory.get()::render);
    }
}
