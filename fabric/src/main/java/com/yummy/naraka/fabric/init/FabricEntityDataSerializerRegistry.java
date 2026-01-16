package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.EntityDataSerializerRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricTrackedDataRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public class FabricEntityDataSerializerRegistry {
    @MethodProxy(EntityDataSerializerRegistry.class)
    public static void register(Identifier name, EntityDataSerializer<?> serializer) {
        FabricTrackedDataRegistry.register(name, serializer);
    }
}
