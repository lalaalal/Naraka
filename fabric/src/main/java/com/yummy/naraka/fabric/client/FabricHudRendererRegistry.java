package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.gui.hud.HudRenderer;
import com.yummy.naraka.client.init.HudRendererRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.HudElementRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.hud.VanillaHudElements;
import net.minecraft.resources.Identifier;

import java.util.function.Supplier;

public final class FabricHudRendererRegistry implements HudRendererRegistry.Registrar {
    @Override
    public void registerPreLayer(Identifier id, Supplier<HudRenderer> factory) {
        HudElementRegistry.attachElementBefore(VanillaHudElements.MISC_OVERLAYS, id, factory.get()::render);
    }

    @Override
    public void registerPostLayer(Identifier id, Supplier<HudRenderer> factory) {
        HudElementRegistry.addLast(id, factory.get()::render);
    }
}
