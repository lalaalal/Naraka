package com.yummy.naraka.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public abstract class EntityAttributeRegistry {
    public static void register(Supplier<? extends EntityType<? extends LivingEntity>> entity, Supplier<AttributeSupplier.Builder> builder) {
        MethodInvoker.invoke(EntityAttributeRegistry.class, "register", entity, builder);
    }
}
