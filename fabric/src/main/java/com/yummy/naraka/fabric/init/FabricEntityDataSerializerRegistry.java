package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.EntityDataSerializerRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;

public class FabricEntityDataSerializerRegistry {
    @MethodProxy(EntityDataSerializerRegistry.class)
    public static void register(ResourceLocation name, EntityDataSerializer<?> serializer) {
        EntityDataSerializers.registerSerializer(serializer);
    }
}
