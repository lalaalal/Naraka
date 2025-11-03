package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.KeyMappingRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;

@OnlyIn(Dist.CLIENT)
public final class NeoForgeKeyMappingRegistry implements NarakaEventBus {
    @MethodProxy(KeyMappingRegistry.class)
    public static void register(KeyMapping keyMapping) {
        NARAKA_BUS.addListener(RegisterKeyMappingsEvent.class, event -> {
            event.register(keyMapping);
        });
    }
}
