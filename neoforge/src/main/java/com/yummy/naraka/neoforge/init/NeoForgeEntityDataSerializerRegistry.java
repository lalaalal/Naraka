package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.NarakaMod;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashMap;
import java.util.Map;

public class NeoForgeEntityDataSerializerRegistry {
    private final Map<String, EntityDataSerializer<?>> serializers = new HashMap<>();

    public void register(String name, EntityDataSerializer<?> serializer) {
        serializers.put(name, serializer);
    }

    public void register(IEventBus bus) {
        DeferredRegister<EntityDataSerializer<?>> registry = DeferredRegister.create(NeoForgeRegistries.ENTITY_DATA_SERIALIZERS, NarakaMod.MOD_ID);
        for (Map.Entry<String, EntityDataSerializer<?>> entry : serializers.entrySet())
            registry.register(entry.getKey(), entry::getValue);
        registry.register(bus);
    }
}
