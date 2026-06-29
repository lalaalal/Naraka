package com.yummy.naraka.forge.init;

import com.yummy.naraka.init.EntityAttributeRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.forge.NarakaEventBus;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.EventPriority;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class ForgeEntityAttributeRegistry implements NarakaEventBus {
    @MethodProxy(EntityAttributeRegistry.class)
    public static void register(Supplier<? extends EntityType<? extends LivingEntity>> entity, Supplier<AttributeSupplier.Builder> builder) {
        NARAKA_BUS.addListener(EventPriority.NORMAL, false, EntityAttributeCreationEvent.class, event -> {
            event.put(entity.get(), builder.get().build());
        });
    }
}
