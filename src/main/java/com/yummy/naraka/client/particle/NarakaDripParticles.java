package com.yummy.naraka.client.particle;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

@Environment(EnvType.CLIENT)
public class NarakaDripParticles {
    public static final float DEFAULT_GRAVITY = 0.06f;

    @Environment(EnvType.CLIENT)
    public static class NarakaDripHangParticle extends DripParticle.DripHangParticle {
        public NarakaDripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle, boolean isGlowing, float gravity) {
            super(level, x, y, z, type, fallingParticle);
            this.isGlowing = isGlowing;
            this.gravity = gravity;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class NarakaFallAndLandParticle extends DripParticle.FallAndLandParticle {
        public NarakaFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, boolean isGlowing, float gravity) {
            super(level, x, y, z, type, landParticle);
            this.isGlowing = isGlowing;
            this.gravity = gravity;
        }
    }

    @Environment(EnvType.CLIENT)
    public static class NarakaDripLandParticle extends DripParticle.DripLandParticle {
        public NarakaDripLandParticle(ClientLevel clientLevel, double d, double e, double f, Fluid fluid, boolean isGlowing, float gravity) {
            super(clientLevel, d, e, f, fluid);
            this.isGlowing = isGlowing;
            this.gravity = gravity;
        }
    }

    @SuppressWarnings("unused")
    public static TextureSheetParticle createNectariumCoreHoneyHangParticle(
            SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
    ) {
        TextureSheetParticle particle = new NarakaDripHangParticle(level, x, y, z, Fluids.EMPTY, NarakaParticleTypes.FALLING_NECTARIUM_CORE_HONEY, true, DEFAULT_GRAVITY * 0.01f);
        particle.setLifetime(100);
        particle.setColor(0.973f, 0.8f, 0.298f);
        return particle;
    }

    @SuppressWarnings("unused")
    public static TextureSheetParticle createNectariumCoreHoneyFallParticle(
            SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
    ) {
        TextureSheetParticle particle = new NarakaFallAndLandParticle(level, x, y, z, Fluids.EMPTY, NarakaParticleTypes.LANDING_NECTARIUM_CORE_HONEY, true, 0.01f);
        particle.setColor(0.973f, 0.8f, 0.298f);
        return particle;
    }

    @SuppressWarnings("unused")
    public static TextureSheetParticle createNectariumCoreHoneyLandParticle(
            SimpleParticleType type, ClientLevel level, double x, double y, double z, double xSpeed, double ySpeed, double zSpeed
    ) {
        TextureSheetParticle particle = new NarakaDripLandParticle(level, x, y, z, Fluids.EMPTY, true, DEFAULT_GRAVITY);
        particle.setLifetime((int) (28.0 / (Math.random() * 0.8 + 0.2)));
        particle.setColor(0.973f, 0.8f, 0.298f);
        return particle;
    }
}
