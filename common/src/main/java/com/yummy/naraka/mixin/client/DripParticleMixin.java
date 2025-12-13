package com.yummy.naraka.mixin.client;

import com.yummy.naraka.client.particle.DripParticleChain;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.core.particles.ParticleOptions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public abstract class DripParticleMixin {
    @Environment(EnvType.CLIENT)
    @Mixin(DripParticle.DripHangParticle.class)
    public static abstract class HoneyHangProviderMixin implements DripParticleChain {
        @Unique @Nullable
        private ParticleOptions naraka$fallingParticle;

        @ModifyArg(
                method = "preMoveUpdate",
                at = @At(value = "INVOKE", target = "Lnet/minecraft/client/multiplayer/ClientLevel;addParticle(Lnet/minecraft/core/particles/ParticleOptions;DDDDDD)V")
        )
        private ParticleOptions modifyFallingParticle(ParticleOptions original) {
            if (naraka$fallingParticle != null)
                return naraka$fallingParticle;
            return original;
        }

        @Override
        public void naraka$set(ParticleOptions particleOptions) {
            this.naraka$fallingParticle = particleOptions;
        }
    }

    @Environment(EnvType.CLIENT)
    @Mixin(DripParticle.HoneyFallAndLandParticle.class)
    public static abstract class HoneyFallProviderMixin implements DripParticleChain {
        @Unique @Nullable
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
}
