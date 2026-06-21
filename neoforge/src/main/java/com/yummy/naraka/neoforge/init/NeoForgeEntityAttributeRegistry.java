package com.yummy.naraka.neoforge.init;

import com.yummy.naraka.init.EntityAttributeRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;

import java.util.function.Supplier;

public final class NeoForgeEntityAttributeRegistry implements EntityAttributeRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(Supplier<? extends EntityType<? extends LivingEntity>> entity, Supplier<AttributeSupplier.Builder> builder) {
        NARAKA_BUS.addListener(EntityAttributeCreationEvent.class, event -> {
            event.put(entity.get(), builder.get().build());
        });
    }
}
