package com.yummy.naraka.init;

import com.yummy.naraka.service.NarakaServices;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public abstract class EntityDataSerializerRegistry {
    public static void register(Identifier name, EntityDataSerializer<?> serializer) {
        NarakaServices.ENTITY_DATA_SERIALIZER_REGISTRY.register(name, serializer);
    }

    public interface Registrar {
        void register(Identifier name, EntityDataSerializer<?> serializer);
    }
}
