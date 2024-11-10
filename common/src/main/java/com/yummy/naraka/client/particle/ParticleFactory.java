package com.yummy.naraka.client.particle;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;

public interface ParticleFactory<T extends ParticleOptions> {
    static <T extends ParticleOptions> ParticleFactory<T> from(ParticleProvider.Sprite<T> provider) {
        return sprite -> (type, level, x, y, z, xSpeed, ySpeed, zSpeed) -> {
            TextureSheetParticle particle = provider.createParticle(type, level, x, y, z, xSpeed, ySpeed, zSpeed);
            if (particle != null)
                particle.pickSprite(sprite);
            return particle;
        };
    }

    ParticleProvider<T> create(SpriteSet sprite);
}
