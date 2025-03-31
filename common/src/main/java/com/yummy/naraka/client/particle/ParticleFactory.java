package com.yummy.naraka.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;

@Environment(EnvType.CLIENT)
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
