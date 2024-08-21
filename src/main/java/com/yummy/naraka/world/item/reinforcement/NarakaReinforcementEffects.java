package com.yummy.naraka.world.item.reinforcement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

public class NarakaReinforcementEffects {
    public static final Holder<ReinforcementEffect> DAMAGE_INCREASE = register(
            "damage_increase", new DamageIncrease()
    );

    private static Holder<ReinforcementEffect> register(String name, ReinforcementEffect effect) {
        return Registry.registerForHolder(NarakaRegistries.REINFORCEMENT_EFFECT, NarakaMod.location(name), effect);
    }

    public static void initialize() {

    }
}
