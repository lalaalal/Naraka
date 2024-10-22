package com.yummy.naraka.core.particles;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class NarakaParticleTypes {
    public static final SimpleParticleType EBONY_LEAVES = register("ebony_leaves", false);

    private static SimpleParticleType register(String name, boolean alwaysSpawn) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, NarakaMod.location(name), new NarakaSimpleParticleType(alwaysSpawn));
    }

    public static void initialize() {

    }

    private static class NarakaSimpleParticleType extends SimpleParticleType {
        public NarakaSimpleParticleType(boolean overrideLimiter) {
            super(overrideLimiter);
        }
    }
}

