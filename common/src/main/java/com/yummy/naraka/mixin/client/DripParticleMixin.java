package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.particle.NarakaDripParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Environment(EnvType.CLIENT)
@Mixin(DripParticle.class)
public abstract class DripParticleMixin {
    @ModifyArg(method = "createHoneyHangParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/DripParticle$DripHangParticle;<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/world/level/material/Fluid;Lnet/minecraft/core/particles/ParticleOptions;)V"), index = 5)
    private static ParticleOptions modifyFallingParticleAfterHang(ParticleOptions original, @Local(argsOnly = true) SimpleParticleType type) {
        if (!NarakaMod.isRegistryLoaded)
            return original;
        if (NarakaDripParticles.REMAP_AFTER_HANG.containsKey(type))
            return NarakaDripParticles.REMAP_AFTER_HANG.get(type);
        return original;
    }

    @ModifyArg(method = "createHoneyFallParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/DripParticle$HoneyFallAndLandParticle;<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/world/level/material/Fluid;Lnet/minecraft/core/particles/ParticleOptions;)V"), index = 5)
    private static ParticleOptions modifyLandingParticleAfterFall(ParticleOptions original, @Local(argsOnly = true) SimpleParticleType type) {
        if (!NarakaMod.isRegistryLoaded)
            return original;
        if (NarakaDripParticles.REMAP_AFTER_FALL.containsKey(type))
            return NarakaDripParticles.REMAP_AFTER_FALL.get(type);
        return original;
    }
}
