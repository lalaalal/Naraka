package com.yummy.naraka.fabric.init;

import com.yummy.naraka.init.EntityDataSerializerRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityDataRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.Identifier;

public class FabricEntityDataSerializerRegistry implements EntityDataSerializerRegistry.Registrar {
    @Override
    public void register(Identifier name, EntityDataSerializer<?> serializer) {
        FabricEntityDataRegistry.register(name, serializer);
    }
}
