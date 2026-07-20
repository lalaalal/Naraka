package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;

public abstract class DimensionSpecialEffectsRegistry {
    public static void register(ResourceLocation location, DimensionSpecialEffects effects) {
        MethodInvoker.of(DimensionSpecialEffectsRegistry.class, "register")
                .withParameterTypes(ResourceLocation.class, DimensionSpecialEffects.class)
                .invoke(location, effects);
    }
}
