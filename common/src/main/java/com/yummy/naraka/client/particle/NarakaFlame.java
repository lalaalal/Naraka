package com.yummy.naraka.client.particle;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.*;
import net.minecraft.client.renderer.LightTexture;

public class NarakaFlame extends RisingParticle {
    public NarakaFlame(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed);
    }

    public void move(double x, double y, double z) {
        this.setBoundingBox(this.getBoundingBox().move(x, y, z));
        this.setLocationFromBoundingbox();
    }

    public float getQuadSize(float scaleFactor) {
        float scale = ((float) this.age + scaleFactor) / (float) this.lifetime;
        return this.quadSize * (1.0F - scale * scale * 0.5F);
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_OPAQUE;
    }

    @Override
    public int getLightColor(float partialTick) {
        return LightTexture.FULL_BRIGHT;
    }

    public static class Provider implements ParticleProvider<NarakaFlameParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(NarakaFlameParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed) {
            NarakaFlame particle = new NarakaFlame(level, x, y, z, xSpeed, ySpeed, zSpeed);
            particle.setSprite(sprites.get(type.ordinal(), NarakaFlameParticleOption.values().length - 1));
            return particle;
        }
    }
}
