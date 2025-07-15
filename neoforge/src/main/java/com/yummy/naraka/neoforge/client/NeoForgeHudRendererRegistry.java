package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.HudRendererRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RenderGuiEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class NeoForgeHudRendererRegistry implements NarakaEventBus {
    private static final Map<ResourceLocation, LayeredDraw.Layer> LAYERS = new HashMap<>();

    private static void render(ResourceLocation id, Supplier<LayeredDraw.Layer> factory, GuiGraphics graphics, DeltaTracker deltaTracker) {
        LayeredDraw.Layer layer = LAYERS.computeIfAbsent(id, key -> factory.get());
        layer.render(graphics, deltaTracker);
    }

    @MethodProxy(HudRendererRegistry.class)
    public static void registerPreLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        NEOFORGE_BUS.addListener(RenderGuiEvent.Pre.class, event -> {
            render(id, factory, event.getGuiGraphics(), event.getPartialTick());
        });
    }

    @MethodProxy(HudRendererRegistry.class)
    public static void registerPostLayer(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        NEOFORGE_BUS.addListener(RenderGuiEvent.Post.class, event -> {
            render(id, factory, event.getGuiGraphics(), event.getPartialTick());
        });
    }
}
