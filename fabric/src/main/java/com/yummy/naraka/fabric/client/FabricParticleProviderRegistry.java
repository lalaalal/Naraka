package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ParticleProviderRegistry;
import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricParticleProviderRegistry {
    @MethodProxy(ParticleProviderRegistry.class)
    public static <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider) {
        net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.getInstance()
                .register(particle.get(), provider);
    }

    @MethodProxy(ParticleProviderRegistry.class)
    public static <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> factory) {
        net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.getInstance()
                .register(particle.get(), factory::create);
    }
}
