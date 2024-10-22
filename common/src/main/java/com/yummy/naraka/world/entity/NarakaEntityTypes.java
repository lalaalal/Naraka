package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.level.entity.EntityAttributeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;

public class NarakaEntityTypes {
    private static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.ENTITY_TYPE);

    public static final RegistrySupplier<EntityType<Herobrine>> HEROBRINE = register(
            "herobrine",
            EntityType.Builder.<Herobrine>of(
                            Herobrine::new,
                            MobCategory.MONSTER
                    )
                    .fireImmune()
                    .sized(0.6f, 2.0f)
    );

    public static final RegistrySupplier<EntityType<Spear>> THROWN_SPEAR = register(
            "spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final RegistrySupplier<EntityType<Spear>> THROWN_MIGHTY_HOLY_SPEAR = register(
            "mighty_holy_spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final RegistrySupplier<EntityType<SpearOfLonginus>> THROWN_SPEAR_OF_LONGINUS = register(
            "spear_of_longinus",
            EntityType.Builder.<SpearOfLonginus>of(SpearOfLonginus::new, MobCategory.MISC)
                    .sized(0.5f, 1.0f)
                    .fireImmune()
                    .immuneTo(Blocks.CACTUS)
    );

    private static <T extends Entity> RegistrySupplier<EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return ENTITY_TYPES.register(name, () -> builder.build(null));
    }

    public static void initialize() {
        ENTITY_TYPES.register();
        EntityAttributeRegistry.register(HEROBRINE, Herobrine::getAttributeSupplier);
    }
}
