package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ParticleProviderRegistry;
import com.yummy.naraka.client.particle.ParticleFactory;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.function.Supplier;

public final class FabricParticleProviderRegistry implements ParticleProviderRegistry.Registrar {
    @Override
    public <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider) {
        net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.getInstance()
                .register(particle.get(), provider);
    }

    @Override
    public <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> factory) {
        net.fabricmc.fabric.api.client.particle.v1.ParticleProviderRegistry.getInstance()
                .register(particle.get(), factory::create);
    }
}
