package com.yummy.naraka.entity;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class NarakaEntities {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, NarakaMod.MOD_ID);

    public static final Supplier<EntityType<Herobrine>> HEROBRINE = ENTITY_TYPES.register(
            "herobrine",
            () -> EntityType.Builder.of(Herobrine::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.0f)
                    .build("herobrine")
    );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
