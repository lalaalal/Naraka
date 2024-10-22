package com.yummy.naraka.world.effect;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.mixin.invoker.MobEffectInvoker;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class NarakaMobEffects {
    private static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(NarakaMod.MOD_ID, Registries.MOB_EFFECT);

    public static final Holder<MobEffect> CHALLENGERS_BLESSING = register(
            "challengers_blessing",
            MobEffectInvoker.create(MobEffectCategory.NEUTRAL, 0xff00ff)
    );

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return MOB_EFFECTS.register(name, () -> effect);
    }

    public static void initialize() {
        MOB_EFFECTS.register();
    }
}
