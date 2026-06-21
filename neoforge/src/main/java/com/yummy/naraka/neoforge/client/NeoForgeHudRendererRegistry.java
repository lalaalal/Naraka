package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.gui.hud.HudRenderer;
import com.yummy.naraka.client.init.HudRendererRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterGuiLayersEvent;

import java.util.function.Supplier;


public final class NeoForgeHudRendererRegistry implements HudRendererRegistry.Registrar, NarakaEventBus {
    @Override
    public void registerPreLayer(Identifier id, Supplier<HudRenderer> factory) {
        NARAKA_BUS.addListener(RegisterGuiLayersEvent.class, event -> {
            event.registerBelowAll(id, factory.get()::render);
        });
    }

    @Override
    public void registerPostLayer(Identifier id, Supplier<HudRenderer> factory) {
        NARAKA_BUS.addListener(RegisterGuiLayersEvent.class, event -> {
            event.registerAboveAll(id, factory.get()::render);
        });
    }
}
