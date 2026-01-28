package com.yummy.naraka.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;

public abstract class EntityDataSerializerRegistry {
    public static void register(ResourceLocation name, EntityDataSerializer<?> serializer) {
        MethodInvoker.of(EntityDataSerializerRegistry.class, "register")
                .withParameterTypes(ResourceLocation.class, EntityDataSerializer.class)
                .invoke(name, serializer);
    }
}
