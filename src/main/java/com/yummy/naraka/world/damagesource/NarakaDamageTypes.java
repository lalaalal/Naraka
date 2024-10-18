package com.yummy.naraka.world.damagesource;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageScaling;
import net.minecraft.world.damagesource.DamageType;

public class NarakaDamageTypes {
    public static final ResourceKey<DamageType> STIGMA = create("stigma");
    public static final ResourceKey<DamageType> SPEAR = create("spear");
    public static final ResourceKey<DamageType> SPEAR_OF_LONGINUS = create("spear_of_longinus");
    public static final ResourceKey<DamageType> MOB_ATTACK_FIXED = create("mob_attack_fixed");

    public static void bootstrap(BootstrapContext<DamageType> context) {
        context.register(STIGMA, new DamageType("stigma", DamageScaling.ALWAYS, 0.1f));
        context.register(SPEAR, new DamageType("spear", 0.1f));
        context.register(SPEAR_OF_LONGINUS, new DamageType("spear_of_longinus", 1));
        context.register(MOB_ATTACK_FIXED, new DamageType("mob_attack_fixed", DamageScaling.ALWAYS, 0.1f));
    }

    private static ResourceKey<DamageType> create(String name) {
        return ResourceKey.create(Registries.DAMAGE_TYPE, NarakaMod.location(name));
    }
}
