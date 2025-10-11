package com.yummy.naraka.client.particle;

import com.yummy.naraka.core.particles.SoulParticleOption;
import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.PortalParticle;
import net.minecraft.client.particle.SpriteSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.RandomSource;

@Environment(EnvType.CLIENT)
public class SoulParticle extends PortalParticle {
    protected SoulParticle(ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, TextureAtlasSprite textureAtlasSprite) {
        super(level, x, y, z, xSpeed, ySpeed, zSpeed, textureAtlasSprite);
    }

    public static class Provider implements ParticleProvider<SoulParticleOption> {
        private final SpriteSet sprites;

        public Provider(SpriteSet spriteSet) {
            this.sprites = spriteSet;
        }

        @Override
        public Particle createParticle(SoulParticleOption type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed, RandomSource randomSource) {
            Color color = Color.of(type.soulType().getColor());
            SoulParticle particle = new SoulParticle(level, x, y, z, xSpeed, ySpeed, zSpeed, sprites.get(randomSource));
            particle.setColor(color.red01(), color.green01(), color.blue01());
            return particle;
        }
    }
}
