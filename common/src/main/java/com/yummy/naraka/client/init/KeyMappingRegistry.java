package com.yummy.naraka.client.init;

import com.yummy.naraka.client.event.ClientEvents;
import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;

import java.util.function.BiConsumer;

@Environment(EnvType.CLIENT)
public abstract class KeyMappingRegistry {
    public static void register(KeyMapping keyMapping, BiConsumer<Minecraft, KeyMapping> handler) {
        MethodInvoker.of(KeyMappingRegistry.class, "register")
                .withParameterTypes(KeyMapping.class)
                .invoke(keyMapping);
        ClientEvents.TICK_POST.register(minecraft -> handler.accept(minecraft, keyMapping));
    }
}
