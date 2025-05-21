package com.yummy.naraka.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.core.particles.SimpleParticleType;

@Environment(EnvType.CLIENT)
public class HerobrineSpawnParticle extends TextureSheetParticle {
    private final double radius;
    private final double speed;
    private static final int SPREAD_TICK = 80;

    public HerobrineSpawnParticle(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        lifetime = 200;
        speed = Math.PI / random.nextInt(10, 15);
        radius = random.nextFloat() * 0.5f + 1.2f;
        setPos(x + radius, y, z);
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;
        if (age >= lifetime)
            remove();

        if (age < SPREAD_TICK) {
            double t = age * speed;
            this.xd = -speed * Math.sin(t) * radius;
            this.zd = speed * Math.cos(t) * radius;
        }

        this.x += xd;
        this.z += zd;

        this.age += 1;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    public static class Provider implements ParticleProvider<SimpleParticleType> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            HerobrineSpawnParticle particle = new HerobrineSpawnParticle(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.pickSprite(sprites);
            return particle;
        }
    }
}
