package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class HudRendererRegistry {
    public static void register(ResourceLocation id, Supplier<LayeredDraw.Layer> factory) {
        MethodInvoker.invoke(HudRendererRegistry.class, "register", id, factory);
    }
}
