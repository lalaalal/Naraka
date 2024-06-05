package com.yummy.naraka.damagesource;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageEffects;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DeathMessageType;

public class NarakaDamageTypes {
    public static final ResourceKey<DamageType> STIGMA = create("stigma");
    public static final ResourceKey<DamageType> DEATH_COUNT_ZERO = create("death_count_zero");
    public static final ResourceKey<DamageType> SPEAR = create("spear");
    public static final ResourceKey<DamageType> SPEAR_OF_LONGINUS = create("spear_of_longinus");

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(STIGMA, new DamageType("stigma", DamageScaling.ALWAYS, 1, DamageEffects.HURT, DeathMessageType.DEFAULT));
        context.register(DEATH_COUNT_ZERO, new DamageType("death_count_zero", 0.1f));
        context.register(SPEAR, new DamageType("spear", 0.1f));
        context.register(SPEAR_OF_LONGINUS, new DamageType("spear_of_longinus", 1));
    }

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, NarakaMod.location(name));
    }
}
