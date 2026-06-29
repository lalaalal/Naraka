package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.KeyMappingRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.client.KeyMapping;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.EventPriority;

public final class ForgeKeyMappingRegistry implements NarakaEventBus {
    @MethodProxy(KeyMappingRegistry.class)
    public static void register(KeyMapping keyMapping) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, RegisterKeyMappingsEvent.class, event -> {
            event.register(keyMapping);
        });
    }
}
