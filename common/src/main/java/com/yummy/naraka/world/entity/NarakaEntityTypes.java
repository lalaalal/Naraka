package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.EntityAttributeRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.block.Blocks;

public class NarakaEntityTypes {
    public static final HolderProxy<EntityType<?>, EntityType<Herobrine>> HEROBRINE = register(
            "herobrine",
            EntityType.Builder.<Herobrine>of(Herobrine::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(0.6f, 1.8f)
                    .eyeHeight(1.62f)
                    .updateInterval(1)
                    .clientTrackingRange(32)
    );

    public static final HolderProxy<EntityType<?>, EntityType<AbsoluteHerobrine>> ABSOLUTE_HEROBRINE = register(
            "absolute_herobrine",
            EntityType.Builder.of(AbsoluteHerobrine::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(0.5f, 1.5f)
                    .updateInterval(1)
    );

    public static final HolderProxy<EntityType<?>, EntityType<ShadowHerobrine>> SHADOW_HEROBRINE = register(
            "shadow_herobrine",
            EntityType.Builder.<ShadowHerobrine>of(ShadowHerobrine::new, MobCategory.MONSTER)
                    .fireImmune()
                    .sized(0.6f, 1.8f)
                    .eyeHeight(1.62f)
                    .updateInterval(1)
                    .clientTrackingRange(32)
    );

    public static final HolderProxy<EntityType<?>, EntityType<Spear>> THROWN_SPEAR = register(
            "spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<Spear>> THROWN_MIGHTY_HOLY_SPEAR = register(
            "mighty_holy_spear",
            EntityType.Builder.<Spear>of(Spear::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.5f, 0.5f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<Stardust>> STARDUST = register(
            "stardust",
            EntityType.Builder.<Stardust>of(Stardust::new, MobCategory.MISC)
                    .sized(0.5f, 0.5f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<NarakaFireball>> NARAKA_FIREBALL = register(
            "naraka_fireball",
            EntityType.Builder.<NarakaFireball>of(NarakaFireball::new, MobCategory.MISC)
                    .sized(0.75f, 0.75f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<SpearOfLonginus>> THROWN_SPEAR_OF_LONGINUS = register(
            "spear_of_longinus",
            EntityType.Builder.<SpearOfLonginus>of(SpearOfLonginus::new, MobCategory.MISC)
                    .sized(0.5f, 1.0f)
                    .updateInterval(1)
                    .fireImmune()
                    .immuneTo(Blocks.CACTUS)
    );

    public static final HolderProxy<EntityType<?>, EntityType<DiamondGolem>> DIAMOND_GOLEM = register(
            "diamond_golem",
            EntityType.Builder.of(DiamondGolem::new, MobCategory.MONSTER)
                    .sized(1.8f, 3f)
                    .updateInterval(1)
                    .clientTrackingRange(10)
    );

    public static final HolderProxy<EntityType<?>, EntityType<MagicCircle>> MAGIC_CIRCLE = register(
            "magic_circle",
            EntityType.Builder.<MagicCircle>of(MagicCircle::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(1, 0.1f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<PickaxeSlash>> PICKAXE_SLASH = register(
            "pickaxe_slash",
            EntityType.Builder.<PickaxeSlash>of(PickaxeSlash::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(5f, 3f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<MassiveLightning>> MASSIVE_LIGHTNING = register(
            "massive_lightning",
            EntityType.Builder.<MassiveLightning>of(MassiveLightning::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0, 0)
    );

    public static final HolderProxy<EntityType<?>, EntityType<ColoredLightningBolt>> COLORED_LIGHTNING_BOLT = register(
            "colored_lightning_bolt",
            EntityType.Builder.<ColoredLightningBolt>of(ColoredLightningBolt::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0, 0)
    );

    public static final HolderProxy<EntityType<?>, EntityType<LightningCircle>> LIGHTNING_CIRCLE = register(
            "lightning_circle",
            EntityType.Builder.<LightningCircle>of(LightningCircle::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0, 0)
    );

    public static final HolderProxy<EntityType<?>, EntityType<NarakaPickaxe>> NARAKA_PICKAXE = register(
            "naraka_pickaxe",
            EntityType.Builder.<NarakaPickaxe>of(NarakaPickaxe::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.5f, 1f)
    );

    public static final HolderProxy<EntityType<?>, EntityType<NarakaSword>> NARAKA_SWORD = register(
            "naraka_sword",
            EntityType.Builder.<NarakaSword>of(NarakaSword::new, MobCategory.MISC)
                    .fireImmune()
                    .sized(0.5f, 0.5f)
    );

    private static <T extends Entity> HolderProxy<EntityType<?>, EntityType<T>> register(String name, EntityType.Builder<T> builder) {
        return RegistryProxy.register(Registries.ENTITY_TYPE, name, () -> builder.build(createKey(name)));
    }

    private static ResourceKey<EntityType<?>> createKey(String name) {
        return ResourceKey.create(Registries.ENTITY_TYPE, NarakaMod.identifier(name));
    }

    public static void initialize() {
        NarakaEntityDataSerializers.initialize();

        EntityAttributeRegistry.register(HEROBRINE, AbstractHerobrine::getAttributeSupplier);
        EntityAttributeRegistry.register(ABSOLUTE_HEROBRINE, AbstractHerobrine::getAttributeSupplier);
        EntityAttributeRegistry.register(SHADOW_HEROBRINE, ShadowHerobrine::getAttributeSupplier);
        EntityAttributeRegistry.register(DIAMOND_GOLEM, DiamondGolem::getAttributeSupplier);
        EntityAttributeRegistry.register(NARAKA_PICKAXE, NarakaPickaxe::getAttributeSupplier);
    }
}
