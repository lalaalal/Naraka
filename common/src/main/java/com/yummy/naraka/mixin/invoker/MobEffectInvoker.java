package com.yummy.naraka.mixin.invoker;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(MobEffect.class)
public interface MobEffectInvoker {
    @Invoker("<init>")
    static MobEffect create(MobEffectCategory category, int color) {
        throw new AssertionError();
    }
}
