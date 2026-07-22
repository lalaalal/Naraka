package com.yummy.naraka.client.init;

import com.yummy.naraka.client.service.NarakaClientServices;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;

public abstract class DimensionSpecialEffectsRegistry {
    public static void register(ResourceLocation location, DimensionSpecialEffects effects) {
        NarakaClientServices.DIMENSION_SPECIAL_EFFECTS_REGISTRY.register(location, effects);
    }

    public interface Registrar {
        void register(ResourceLocation location, DimensionSpecialEffects effects);
    }
}
