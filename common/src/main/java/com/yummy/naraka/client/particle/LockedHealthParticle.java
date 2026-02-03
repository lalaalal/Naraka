package com.yummy.naraka.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SingleQuadParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.util.RandomSource;
import org.jspecify.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class LockedHealthParticle extends SingleQuadParticle {
    private final SpriteSet sprites;

    protected LockedHealthParticle(ClientLevel level, double x, double y, double z, SpriteSet sprites) {
        super(level, x, y, z, sprites.get(0, 1));
        this.sprites = sprites;
        this.gravity = 0;
        this.lifetime = 50;
        this.yd = 0.75;
        this.scale(2);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.age < 20) {
            this.yd *= 0.5;
        } else if (this.age < 40) {
            if (this.age == 20) {
                setSprite(sprites.get(1, 1));
                for (int count = 0; count < 36; count++) {
                    double xSpeed = random.nextDouble() * 0.5 - 0.25;
                    double zSpeed = random.nextDouble() * 0.5 - 0.25;
                    double ySpeed = random.nextDouble() * 0.25;
                    level.addParticle(ParticleTypes.FIREWORK, x, y, z, xSpeed, ySpeed, zSpeed);
                }
            }
            this.yd = 0;
            if (this.age % 2 == 0)
                y += 0.1;
            else
                y -= 0.1;
        }
    }

    @Override
    protected Layer getLayer() {
        return Layer.OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public @Nullable Particle createParticle(SimpleParticleType particleType, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource random) {
            return new LockedHealthParticle(level, x, y, z, this.sprites);
        }
    }
}
