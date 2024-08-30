package com.yummy.naraka.client.particle;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluid;

@Environment(EnvType.CLIENT)
public class NarakaDripParticles {
    public static final float DEFAULT_GRAVITY = 0.06f;

    public static class NarakaDripHangParticle extends DripParticle.DripHangParticle {
        public NarakaDripHangParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle, boolean isGlowing, float gravity) {
            super(level, x, y, z, type, fallingParticle);
            this.isGlowing = isGlowing;
            this.gravity = gravity;
        }
    }

    public static class NarakaFallAndLandParticle extends DripParticle.FallAndLandParticle {
        public NarakaFallAndLandParticle(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions landParticle, boolean isGlowing, float gravity) {
            super(level, x, y, z, type, landParticle);
            this.isGlowing = isGlowing;
            this.gravity = gravity;
        }
    }

    public static class NarakaDripLandParticle extends DripParticle.DripLandParticle {
        public NarakaDripLandParticle(ClientLevel clientLevel, double d, double e, double f, Fluid fluid, boolean isGlowing, float gravity) {
            super(clientLevel, d, e, f, fluid);
            this.isGlowing = isGlowing;
            this.gravity = gravity;
        }
    }
}
