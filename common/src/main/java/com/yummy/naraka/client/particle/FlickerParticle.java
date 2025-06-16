package com.yummy.naraka.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.core.particles.SimpleParticleType;

public class FlickerParticle extends TextureSheetParticle {
    private final SpriteSet sprites;

    protected FlickerParticle(ClientLevel clientLevel, SpriteSet sprites, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
        this.sprites = sprites;
        this.setSpriteFromAge(this.sprites);
        this.quadSize = 4;
        this.lifetime = 5;
    }

    public void tick() {
        this.oRoll = this.roll;
        if (this.age < this.lifetime) {
            this.setSpriteFromAge(this.sprites);
        } else {
            this.remove();
        }
        age += 1;
    }

    @Override
    protected int getLightColor(float partialTick) {
        return LightTexture.pack(15, 15);
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
            return new FlickerParticle(level, sprites, x, y, z, xSpeed, ySpeed, zSpeed);
        }
    }
}
