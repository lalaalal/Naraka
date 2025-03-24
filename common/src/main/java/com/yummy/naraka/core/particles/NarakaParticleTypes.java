package com.yummy.naraka.core.particles;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

import java.util.function.Function;

public class NarakaParticleTypes {
    public static final LazyHolder<ParticleType<?>, SimpleParticleType> EBONY_LEAVES = register("ebony_leaves", false);
    public static final LazyHolder<ParticleType<?>, SimpleParticleType> DRIPPING_NECTARIUM = register("dripping_nectarium", false);
    public static final LazyHolder<ParticleType<?>, SimpleParticleType> FALLING_NECTARIUM = register("falling_nectarium", false);
    public static final LazyHolder<ParticleType<?>, SimpleParticleType> LANDING_NECTARIUM = register("landing_nectarium", false);

    public static final LazyHolder<ParticleType<?>, ParticleType<SoulParticleOption>> SOUL = register("soul", false, SoulParticleOption::type);

    private static <T extends ParticleType<?>> LazyHolder<ParticleType<?>, T> register(String name, boolean force, Function<Boolean, T> factory) {
        return RegistryProxy.register(Registries.PARTICLE_TYPE, name, () -> factory.apply(force));
    }

    private static LazyHolder<ParticleType<?>, SimpleParticleType> register(String name, boolean force) {
        return RegistryProxy.register(Registries.PARTICLE_TYPE, name, () -> new NarakaSimpleParticleType(force));
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.PARTICLE_TYPE)
                .onRegistrationFinished();
    }

    private static class NarakaSimpleParticleType extends SimpleParticleType {
        public NarakaSimpleParticleType(boolean force) {
            super(force);
        }
    }


}

