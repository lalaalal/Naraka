package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.DimensionSpecialEffectsRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.Identifier;

import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public final class FabricDimensionSpecialEffectsRegistry {
    private static final Map<Identifier, DimensionSpecialEffects> CUSTOM_DIMENSION_EFFECTS = new HashMap<>();

    @MethodProxy(DimensionSpecialEffectsRegistry.class)
    public static void register(Identifier location, DimensionSpecialEffects effects) {
        CUSTOM_DIMENSION_EFFECTS.put(location, effects);
    }

    public static boolean hasCustomDimensionEffects(Identifier location) {
        return CUSTOM_DIMENSION_EFFECTS.containsKey(location);
    }

    public static DimensionSpecialEffects getCustomDimensionEffects(Identifier location) {
        return CUSTOM_DIMENSION_EFFECTS.get(location);
    }
}
