package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.gui.hud.HudRenderer;
import com.yummy.naraka.client.init.HudRendererRegistry;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ForgeHudRendererRegistry implements NarakaEventBus {
    @MethodProxy(HudRendererRegistry.class)
    public static void registerPreLayer(ResourceLocation id, Supplier<HudRenderer> factory) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, RegisterGuiOverlaysEvent.class, event -> {
            HudRenderer hudRenderer = factory.get();
            event.registerBelowAll(id.getPath(), (gui, graphics, partialTick, screenWidth, screenHeight) -> hudRenderer.render(graphics, partialTick));
        });
    }

    @MethodProxy(HudRendererRegistry.class)
    public static void registerPostLayer(ResourceLocation id, Supplier<HudRenderer> factory) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, RegisterGuiOverlaysEvent.class, event -> {
            HudRenderer hudRenderer = factory.get();
            event.registerAboveAll(id.getPath(), (gui, graphics, partialTick, screenWidth, screenHeight) -> hudRenderer.render(graphics, partialTick));
        });
    }
}
