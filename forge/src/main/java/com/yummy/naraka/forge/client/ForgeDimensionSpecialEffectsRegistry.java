package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.DimensionSpecialEffectsRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.client.renderer.DimensionSpecialEffects;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterDimensionSpecialEffectsEvent;
import net.minecraftforge.eventbus.api.EventPriority;

@OnlyIn(Dist.CLIENT)
public class ForgeDimensionSpecialEffectsRegistry implements NarakaEventBus {
    @MethodProxy(DimensionSpecialEffectsRegistry.class)
    public static void register(ResourceLocation location, DimensionSpecialEffects effects) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, RegisterDimensionSpecialEffectsEvent.class, event -> {
            event.register(location, effects);
        });
    }
}
