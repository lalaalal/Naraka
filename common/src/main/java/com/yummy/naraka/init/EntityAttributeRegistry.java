package com.yummy.naraka.init;

import com.yummy.naraka.service.NarakaServices;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public abstract class EntityAttributeRegistry {
    public static void register(Supplier<? extends EntityType<? extends LivingEntity>> entity, Supplier<AttributeSupplier.Builder> builder) {
        NarakaServices.ENTITY_ATTRIBUTE_REGISTRY.register(entity, builder);
    }

    public interface Registrar {
        void register(Supplier<? extends EntityType<? extends LivingEntity>> entity, Supplier<AttributeSupplier.Builder> builder);
    }
}
