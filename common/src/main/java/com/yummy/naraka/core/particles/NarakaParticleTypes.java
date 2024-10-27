package com.yummy.naraka.core.particles;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.init.RegistryInitializer;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class NarakaParticleTypes {
    public static final LazyHolder<ParticleType<?>, SimpleParticleType> EBONY_LEAVES = register("ebony_leaves", false);

    private static LazyHolder<ParticleType<?>, SimpleParticleType> register(String name, boolean alwaysSpawn) {
        return RegistryProxy.register(Registries.PARTICLE_TYPE, name, () -> new NarakaSimpleParticleType(alwaysSpawn));
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.PARTICLE_TYPE)
                .onRegistrationFinished();
    }

    private static class NarakaSimpleParticleType extends SimpleParticleType {
        public NarakaSimpleParticleType(boolean overrideLimiter) {
            super(overrideLimiter);
        }
    }
}

