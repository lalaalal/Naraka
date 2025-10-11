package com.yummy.naraka.client.particle;

import com.yummy.naraka.core.particles.NarakaFlameParticleOption;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;

public class NarakaFlame extends FlameParticle {
    public NarakaFlame(ClientLevel clientLevel, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite sprite) {
        super(clientLevel, x, y, z, xSpeed, ySpeed, zSpeed, sprite);
    }

    public static class Provider implements ParticleProvider<NarakaFlameParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet sprites) {
            this.sprites = sprites;
        }

        @Override
        public Particle createParticle(NarakaFlameParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
            NarakaFlame particle = new NarakaFlame(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.first());
            particle.setSprite(sprites.get(type.ordinal(), NarakaFlameParticleOption.values().length - 1));
            return particle;
        }
    }
}
