package com.yummy.naraka.fabric.init;

import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.init.EntityAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;

import java.util.function.Supplier;

public final class FabricEntityAttributeRegistry implements EntityAttributeRegistry.Registrar {
    @Override
    public void register(ValueGetter<? extends EntityType<? extends LivingEntity>> entity, Supplier<AttributeSupplier.Builder> builder) {
        FabricDefaultAttributeRegistry.register(entity.getConcreteValue(), builder.get());
    }
}
