package com.yummy.naraka.mixin.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.particle.NarakaDripParticles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Environment(EnvType.CLIENT)
@Mixin(DripParticle.class)
public abstract class DripParticleMixin {
    @Unique @Nullable
    private static SimpleParticleType naraka$currentParticle = null;

    @Inject(method = "createHoneyHangParticle", at = @At("HEAD"))
    private static void storeHangingParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<TextureSheetParticle> cir) {
        naraka$currentParticle = type;
    }

    @ModifyArg(method = "createHoneyHangParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/DripParticle$DripHangParticle;<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/world/level/material/Fluid;Lnet/minecraft/core/particles/ParticleOptions;)V"), index = 5)
    private static ParticleOptions modifyFallingParticleAfterHang(ParticleOptions original) {
        if (!NarakaMod.isRegistryLoaded)
            return original;
        if (NarakaDripParticles.REMAP_AFTER_HANG.containsKey(naraka$currentParticle))
            return NarakaDripParticles.REMAP_AFTER_HANG.get(naraka$currentParticle);
        naraka$currentParticle = null;
        return original;
    }

    @Inject(method = "createHoneyFallParticle", at = @At("HEAD"))
    private static void storeFallingParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, CallbackInfoReturnable<TextureSheetParticle> cir) {
        naraka$currentParticle = type;
    }

    @ModifyArg(method = "createHoneyFallParticle", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/particle/DripParticle$HoneyFallAndLandParticle;<init>(Lnet/minecraft/client/multiplayer/ClientLevel;DDDLnet/minecraft/world/level/material/Fluid;Lnet/minecraft/core/particles/ParticleOptions;)V"), index = 5)
    private static ParticleOptions modifyLandingParticleAfterFall(ParticleOptions original) {
        if (!NarakaMod.isRegistryLoaded)
            return original;
        if (NarakaDripParticles.REMAP_AFTER_FALL.containsKey(naraka$currentParticle))
            return NarakaDripParticles.REMAP_AFTER_FALL.get(naraka$currentParticle);
        naraka$currentParticle = null;
        return original;
    }
}
