package com.yummy.naraka.client.init;

import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public abstract class ParticleProviderRegistry {
    public static <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider) {
        MethodInvoker.of(ParticleProviderRegistry.class, "register")
                .withParameterTypes(Supplier.class, ParticleProvider.class)
                .invoke(particle, provider);
    }

    public static <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> factory) {
        MethodInvoker.of(ParticleProviderRegistry.class, "register")
                .withParameterTypes(Supplier.class, ParticleFactory.class)
                .invoke(particle, factory);
    }

    public static <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleProvider.Sprite<T> provider) {
        register(particle, ParticleFactory.from(provider));
    }
}
