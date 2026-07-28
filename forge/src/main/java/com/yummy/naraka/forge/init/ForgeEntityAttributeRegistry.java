package com.yummy.naraka.forge.init;

import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.init.EntityAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.function.Supplier;

public class ForgeEntityAttributeRegistry implements EntityAttributeRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(ValueGetter<? extends EntityType<? extends LivingEntity>> entity, Supplier<AttributeSupplier.Builder> builder) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, EntityAttributeCreationEvent.class, event -> {
            event.put(entity.getConcreteValue(), builder.get().build());
        });
    }
}
