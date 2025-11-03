package com.yummy.naraka.client.particle;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class NectariumParticle {
    public static ParticleProvider<SimpleParticleType> createNectariumHangParticle(SpriteSet sprite) {
        return (particleOptions, clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, randomSource) -> {
            DripParticle.HoneyHangProvider provider = new DripParticle.HoneyHangProvider(sprite);
            SingleQuadParticle particle = (SingleQuadParticle) provider.createParticle(particleOptions, clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, randomSource);
            particle.setColor(0.7f, 0.4f, 0.7f);
            if (particle instanceof DripParticleChain dripParticleChain)
                dripParticleChain.naraka$set(NarakaParticleTypes.FALLING_NECTARIUM.get());
            return particle;
        };
    }

    public static ParticleProvider<SimpleParticleType> createNectariumFallParticle(SpriteSet sprite) {
        return (particleOptions, clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, randomSource) -> {
            DripParticle.HoneyFallProvider provider = new DripParticle.HoneyFallProvider(sprite);
            SingleQuadParticle particle = (SingleQuadParticle) provider.createParticle(particleOptions, clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, randomSource);
            particle.setColor(0.7f, 0.4f, 0.7f);
            if (particle instanceof DripParticleChain dripParticleChain)
                dripParticleChain.naraka$set(NarakaParticleTypes.LANDING_NECTARIUM.get());
            return particle;
        };
    }

    public static ParticleProvider<SimpleParticleType> createNectariumLandParticle(SpriteSet sprite) {
        return (particleOptions, clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, randomSource) -> {
            DripParticle.HoneyLandProvider provider = new DripParticle.HoneyLandProvider(sprite);
            SingleQuadParticle particle = (SingleQuadParticle) provider.createParticle(particleOptions, clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, randomSource);
            particle.setColor(0.7f, 0.4f, 0.7f);
            return particle;
        };
    }
}
