package com.yummy.naraka.entity;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaEntities {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, NarakaMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<Herobrine>> HEROBRINE = ENTITY_TYPES.register(
            "herobrine",
            () -> EntityType.Builder.of(Herobrine::new, MobCategory.MONSTER)
                    .sized(0.6f, 2.0f)
                    .build("herobrine")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Spear>> THROWN_SPEAR = ENTITY_TYPES.register(
            "spear",
            () -> EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build("spear")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<Spear>> THROWN_MIGHTY_HOLY_SPEAR = ENTITY_TYPES.register(
            "mighty_holy_spear",
            () -> EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .build("mighty_holy_spear")
    );

    public static final DeferredHolder<EntityType<?>, EntityType<SpearOfLonginus>> THROWN_SPEAR_OF_LONGINUS = ENTITY_TYPES.register(
            "spear_of_longinus",
            () -> EntityType.Builder.<SpearOfLonginus>of(SpearOfLonginus::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
                    .fireImmune()
                    .immuneTo(Blocks.CACTUS)
                    .build("spear_of_longinus")
    );

    public static void register(IEventBus bus) {
        ENTITY_TYPES.register(bus);
    }
}
