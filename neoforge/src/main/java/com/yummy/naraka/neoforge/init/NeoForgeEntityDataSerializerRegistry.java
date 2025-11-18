package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.init.EntityDataSerializerRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import net.neoforged.neoforge.registries.RegisterEvent;

public class NeoForgeEntityDataSerializerRegistry implements NarakaEventBus {
    @MethodProxy(EntityDataSerializerRegistry.class)
    public static void register(ResourceLocation name, EntityDataSerializer<?> serializer) {
        NARAKA_BUS.addListener(RegisterEvent.class, event -> {
            event.register(NeoForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, name, () -> serializer);
        });
    }
}
