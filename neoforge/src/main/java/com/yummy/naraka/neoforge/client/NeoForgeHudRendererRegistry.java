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

    @MethodProxy(HudRendererRegistry.class)
    public static void register(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        NEOFORGE_BUS.addListener(RenderGuiEvent.Post.class, event -> {
            LayeredDraw.Layer layer = LAYERS.computeIfAbsent(id, key -> factory.get());
            GuiGraphics graphics = event.getGuiGraphics();
            DeltaTracker deltaTracker = event.getPartialTick();
            layer.render(graphics, deltaTracker);
        });
    }
}
