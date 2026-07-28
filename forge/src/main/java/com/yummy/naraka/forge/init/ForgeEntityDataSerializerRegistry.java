package com.yummy.naraka.forge.init;

import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.init.EntityDataSerializerRegistry;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;

public final class ForgeEntityDataSerializerRegistry implements EntityDataSerializerRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(ResourceLocation name, EntityDataSerializer<?> serializer) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, RegisterEvent.class, event -> {
            event.register(ForgeRegistries.Keys.ENTITY_DATA_SERIALIZERS, name, () -> serializer);
        });
    }
}
