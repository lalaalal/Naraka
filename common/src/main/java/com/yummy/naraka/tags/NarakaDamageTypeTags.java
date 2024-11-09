package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.damagesource.DamageType;

public final class NarakaDamageTypeTags {
    public static final TagKey<DamageType> DEATH_COUNTING_ATTACK = create("death_counting_attack");

    public static TagKey<DamageType> create(String name) {
        return TagKey.create(Registries.DAMAGE_TYPE, NarakaMod.location(name));
    }
}
