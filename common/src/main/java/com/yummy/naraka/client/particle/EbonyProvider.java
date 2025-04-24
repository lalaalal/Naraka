package com.yummy.naraka.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FallingLeavesParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.SimpleParticleType;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class EbonyProvider implements ParticleProvider<SimpleParticleType> {
    private final SpriteSet sprites;

    public EbonyProvider(SpriteSet sprites) {
        this.sprites = sprites;
    }

    @Nullable
    @Override
    public Particle createParticle(SimpleParticleType particleOptions, ClientLevel level, double x, double y, double z, double dx, double dy, double dz) {
        return new FallingLeavesParticle(level, x, y, z, this.sprites, 0.25F, 2.0F, false, true, 1.0F, 0.0F);
    }
}
