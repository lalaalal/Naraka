package com.yummy.naraka.world.entity;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;

public class NarakaEntityTypes {
    public static final LazyHolder<EntityType<?>, EntityType<Herobrine>> HEROBRINE = register(
            "herobrine",
            EntityType.Builder.<Herobrine>of(
                            Herobrine::new,
                            MobCategory.MONSTER
                    )
                    .fireImmune()
                    .sized(0.6f, 2.0f)
    );

    public static final LazyHolder<EntityType<?>, EntityType<Spear>> THROWN_SPEAR = register(
            "spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final LazyHolder<EntityType<?>, EntityType<Spear>> THROWN_MIGHTY_HOLY_SPEAR = register(
            "mighty_holy_spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final LazyHolder<EntityType<?>, EntityType<Stardust>> STARDUST = register(
            "stardust",
            EntityType.Builder.<Stardust>of(Stardust::new, MobCategory.MISC)
                    .sized(1, 1)
    );

    public static final LazyHolder<EntityType<?>, EntityType<NarakaFireball>> NARAKA_FIREBALL = register(
            "naraka_fireball",
            EntityType.Builder.<NarakaFireball>of(NarakaFireball::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final LazyHolder<EntityType<?>, EntityType<SpearOfLonginus>> THROWN_SPEAR_OF_LONGINUS = register(
            "spear_of_longinus",
            EntityType.Builder.<SpearOfLonginus>of(SpearOfLonginus::new, MobCategory.MISC)
                    .sized(0.5f, 1.0f)
                    .fireImmune()
                    .immuneTo(Blocks.CACTUS)
    );

    private static <T extends Entity> LazyHolder<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return RegistryProxy.register(Registries.ENTITY_TYPE, name, () -> builder.build(null));
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.ENTITY_TYPE)
                .onRegistrationFinished();
        EntityAttributeRegistry.register(HEROBRINE, Herobrine::getAttributeSupplier);
    }
}
