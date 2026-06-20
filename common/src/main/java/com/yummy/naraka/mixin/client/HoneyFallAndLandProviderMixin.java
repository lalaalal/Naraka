package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.particle.DripParticleChain;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(DripParticle.HoneyFallAndLandParticle.class)
public abstract class HoneyFallAndLandProviderMixin implements DripParticleChain {
    @Unique
    @Nullable
    private ParticleOptions naraka$landingParticle;

    @ModifyArg(
            method = "postMoveUpdate",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V")
    )
    private ParticleOptions modifyLandingParticle(ParticleOptions original) {
        if (naraka$landingParticle != null)
            return naraka$landingParticle;
        return original;
    }

    @Override
    public void naraka$set(ParticleOptions particleOptions) {
        this.naraka$landingParticle = particleOptions;
    }
}