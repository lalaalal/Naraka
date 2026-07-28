package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ParticleProviderRegistry;
import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.core.registries.ValueGetter;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricParticleProviderRegistry implements ParticleProviderRegistry.Registrar {
    @Override
    public <T extends ParticleOptions> void register(ValueGetter<? extends ParticleType<T>> particle, ParticleProvider<T> provider) {
        ParticleFactoryRegistry.getInstance().register(particle.getConcreteValue(), provider);
    }

    @Override
    public <T extends ParticleOptions> void register(ValueGetter<? extends ParticleType<T>> particle, ParticleFactory<T> factory) {
        ParticleFactoryRegistry.getInstance().register(particle.getConcreteValue(), factory::create);
    }
}
