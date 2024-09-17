package com.yummy.naraka.world.effect;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class NarakaMobEffects {
    public static final Holder<MobEffect> CHALLENGERS_BLESSING = register(
            "challengers_blessing",
            new MobEffect(MobEffectCategory.NEUTRAL, 0xff00ff)
    );

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return Registry.registerForHolder(BuiltInRegistries.MOB_EFFECT, NarakaMod.location(name), effect);
    }

    public static void initialize() {

    }
}
