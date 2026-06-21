package com.yummy.naraka.client.init;

import com.yummy.naraka.client.NarakaClientServices;
import com.yummy.naraka.client.event.ClientEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.function.BiConsumer;

public abstract class KeyMappingRegistry {
    public static void register(KeyMapping keyMapping, BiConsumer<Minecraft, KeyMapping> handler) {
        NarakaClientServices.KEY_MAPPING_REGISTRY.register(keyMapping);
        ClientEvents.TICK_POST.register(minecraft -> handler.accept(minecraft, keyMapping));
    }

    public interface Registrar {
        void register(KeyMapping keyMapping);
    }
}
