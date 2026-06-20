package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.DimensionSpecialEffectsRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterDimensionSpecialEffectsEvent;

@OnlyIn(Dist.CLIENT)
public class NeoForgeDimensionSpecialEffectsRegistry implements NarakaEventBus {
    @MethodProxy(DimensionSpecialEffectsRegistry.class)
    public static void register(ResourceLocation location, DimensionSpecialEffects effects) {
        NARAKA_BUS.addListener(RegisterDimensionSpecialEffectsEvent.class, event -> {
            event.register(location, effects);
        });
    }
}
