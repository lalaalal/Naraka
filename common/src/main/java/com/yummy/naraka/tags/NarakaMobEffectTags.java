package com.yummy.naraka.tags;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffect;

public final class NarakaMobEffectTags {
    public static final TagKey<MobEffect> CHALLENGERS_BLESSING = create("challengers_blessing");

    private static TagKey<MobEffect> create(String name) {
        return TagKey.create(Registries.MOB_EFFECT, NarakaMod.location(name));
    }
}
