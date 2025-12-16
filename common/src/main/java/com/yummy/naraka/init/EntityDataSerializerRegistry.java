package com.yummy.naraka.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public abstract class EntityDataSerializerRegistry {
    public static void register(Identifier name, EntityDataSerializer<?> serializer) {
        MethodInvoker.of(EntityDataSerializerRegistry.class, "register")
                .withParameterTypes(Identifier.class, EntityDataSerializer.class)
                .invoke(name, serializer);
    }
}
