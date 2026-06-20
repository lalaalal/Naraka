package com.yummy.naraka.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleOptions;

public interface ParticleFactory<T extends ParticleOptions> {
    @Deprecated
    static <T extends ParticleOptions> ParticleFactory<T> from(ParticleProvider.Sprite<T> provider) {
        return sprite -> (type, level, x, y, z, xSpeed, ySpeed, zSpeed, randomSource) -> {
            SingleQuadParticle particle = provider.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed, randomSource);
            if (particle != null)
                particle.setSpriteFromAge(sprite);
            return particle;
        };
    }

    ParticleProvider<T> create(SpriteSet sprite);
}
