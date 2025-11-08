package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.DimensionSpecialEffectsRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.DimensionRenderingRegistry;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public final class FabricDimensionSpecialEffectsRegistry {
    @MethodProxy(DimensionSpecialEffectsRegistry.class)
    public static void register(ResourceLocation location, DimensionSpecialEffects effects) {
        DimensionRenderingRegistry.registerDimensionEffects(location, effects);
    }
}
