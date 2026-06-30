package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.NarakaClientContext;
import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ParticleEngine.class)
public abstract class ParticleEngineMixin {
    @Inject(method = "tick", at = @At("HEAD"), cancellable = true)
    public void preventTickOnFrozen(CallbackInfo ci) {
        if (NarakaClientContext.TICK_FROZEN.getValue())
            ci.cancel();
    }
}
