package com.yummy.naraka.client.init;

import com.yummy.naraka.client.NarakaClientServices;
import com.yummy.naraka.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

public abstract class ParticleProviderRegistry {
    public static <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider) {
        NarakaClientServices.PARTICLE_PROVIDER_REGISTRY.register(particle, provider);
    }

    public static <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> factory) {
        NarakaClientServices.PARTICLE_PROVIDER_REGISTRY.register(particle, factory);
    }

    @Deprecated
    public static <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleProvider.Sprite<T> provider) {
        register(particle, ParticleFactory.from(provider));
    }

    public interface Registrar {
        <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider);

        <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> factory);
    }
}
