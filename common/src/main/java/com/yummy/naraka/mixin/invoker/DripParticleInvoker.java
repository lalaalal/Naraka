package com.yummy.naraka.mixin.invoker;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.DripParticle;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.world.level.material.Fluid;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

public abstract class DripParticleInvoker {
    @Mixin(DripParticle.DripHangParticle.class)
    public interface DripHangParticleInvoker {
        @Invoker("<init>")
        static DripParticle.DripHangParticle create(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle) {
            throw new AssertionError();
        }
    }

    @Mixin(DripParticle.HoneyFallAndLandParticle.class)
    public interface HoneyFallAndLandParticleInvoker {
        @Invoker("<init>")
        static DripParticle.HoneyFallAndLandParticle create(ClientLevel level, double x, double y, double z, Fluid type, ParticleOptions fallingParticle) {
            throw new AssertionError();
        }
    }

    @Mixin(DripParticle.DripLandParticle.class)
    public interface DripLandParticleInvoker {
        @Invoker("<init>")
        static DripParticle.DripLandParticle create(ClientLevel level, double x, double y, double z, Fluid type) {
            throw new AssertionError();
        }
    }
}
