package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.EntityDataSerializerRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.resources.ResourceLocation;

public class FabricEntityDataSerializerRegistry implements EntityDataSerializerRegistry.Registrar {
    @Override
    public void register(ResourceLocation name, EntityDataSerializer<?> serializer) {
        EntityDataSerializers.registerSerializer(serializer);
    }
}
