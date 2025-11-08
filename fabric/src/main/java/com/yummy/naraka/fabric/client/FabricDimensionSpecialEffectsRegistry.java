package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.DimensionSpecialEffectsRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class FabricDimensionSpecialEffectsRegistry {
    private static final Map<ResourceLocation, DimensionSpecialEffects> CUSTOM_DIMENSION_EFFECTS = new HashMap<>();

    @MethodProxy(DimensionSpecialEffectsRegistry.class)
    public static void register(ResourceLocation location, DimensionSpecialEffects effects) {
        CUSTOM_DIMENSION_EFFECTS.put(location, effects);
    }

    public static boolean hasCustomDimensionEffects(ResourceLocation location) {
        return CUSTOM_DIMENSION_EFFECTS.containsKey(location);
    }

    public static DimensionSpecialEffects getCustomDimensionEffects(ResourceLocation location) {
        return CUSTOM_DIMENSION_EFFECTS.get(location);
    }
}
