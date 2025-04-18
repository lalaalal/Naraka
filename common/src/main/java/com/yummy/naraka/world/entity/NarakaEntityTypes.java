package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import com.yummy.naraka.init.EntityAttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;

public class NarakaEntityTypes {
    public static final HolderProxy<EntityType<?>, EntityType<Herobrine>> HEROBRINE = register(
            "herobrine",
            EntityType.Builder.<Herobrine>of(
                            Herobrine::new,
                            MobCategory.MONSTER
                    )
                    .fireImmune()
                    .sized(0.6f, 2.0f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<ShadowHerobrine>> SHADOW_HEROBRINE = register(
            "shadow_herobrine",
            EntityType.Builder.<ShadowHerobrine>of(
                            ShadowHerobrine::new,
                            MobCategory.MONSTER
                    )
                    .fireImmune()
                    .sized(0.6f, 2.0f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<Spear>> THROWN_SPEAR = register(
            "spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<Spear>> THROWN_MIGHTY_HOLY_SPEAR = register(
            "mighty_holy_spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<Stardust>> STARDUST = register(
            "stardust",
            EntityType.Builder.<Stardust>of(Stardust::new, MobCategory.MISC)
                    .sized(1, 1)
    );

    public static final HolderProxy<EntityType<?>, EntityType<NarakaFireball>> NARAKA_FIREBALL = register(
            "naraka_fireball",
            EntityType.Builder.<NarakaFireball>of(NarakaFireball::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<SpearOfLonginus>> THROWN_SPEAR_OF_LONGINUS = register(
            "spear_of_longinus",
            EntityType.Builder.<SpearOfLonginus>of(SpearOfLonginus::new, MobCategory.MISC)
                    .sized(0.5f, 1.0f)
                    .fireImmune()
                    .immuneTo(Blocks.CACTUS)
    );

    private static <T extends Entity> HolderProxy<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return RegistryProxy.register(Registries.ENTITY_TYPE, name, () -> builder.build(NarakaMod.MOD_ID + ":" + name));
    }

    public static void initialize() {
        RegistryProxyProvider.get(Registries.ENTITY_TYPE)
                .onRegistrationFinished();
        EntityAttributeRegistry.register(HEROBRINE, AbstractHerobrine::getAttributeSupplier);
        EntityAttributeRegistry.register(SHADOW_HEROBRINE, ShadowHerobrine::getAttributeSupplier);
    }
}
