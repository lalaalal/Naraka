package com.yummy.naraka.world.effect;

import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import com.yummy.naraka.mixin.invoker.MobEffectInvoker;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;

public class NarakaMobEffects {
    public static final Holder<MobEffect> CHALLENGERS_BLESSING = register(
            "challengers_blessing",
            MobEffectInvoker.create(MobEffectCategory.NEUTRAL, 0xff00ff)
    );

    private static Holder<MobEffect> register(String name, MobEffect effect) {
        return RegistryProxy.register(Registries.MOB_EFFECT, name, () -> effect);
    }

    public static void initialize() {
        RegistryProxyProvider.get(Registries.MOB_EFFECT)
                .onRegistrationFinished();
    }
}
