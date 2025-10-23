package com.yummy.naraka.client.particle;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class NectariumParticle {
    public static TextureSheetParticle createNectariumHangParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        TextureSheetParticle particle = DripParticle.createHoneyHangParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
        if (particle instanceof DripParticleChain dripParticleChain)
            dripParticleChain.naraka$set(NarakaParticleTypes.FALLING_NECTARIUM.get());
        particle.setLifetime(100);
        particle.setColor(0.7f, 0.4f, 0.7f);
        return particle;
    }

    public static TextureSheetParticle createNectariumFallParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        TextureSheetParticle particle = DripParticle.createHoneyFallParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
        if (particle instanceof DripParticleChain dripParticleChain)
            dripParticleChain.naraka$set(NarakaParticleTypes.LANDING_NECTARIUM.get());
        particle.setColor(0.7f, 0.4f, 0.7f);
        return particle;
    }

    public static TextureSheetParticle createNectariumLandParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        TextureSheetParticle particle = DripParticle.createHoneyLandParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
        particle.setColor(0.7f, 0.4f, 0.7f);
        return particle;
    }
}
