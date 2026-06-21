package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.ParticleProviderRegistry;
import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NeoForgeParticleProviderRegistry implements ParticleProviderRegistry.Registrar, NarakaEventBus {
    @Override
    public <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleProvider<T> provider) {
        NARAKA_BUS.addListener((Consumer<RegisterParticleProvidersEvent>) event -> event.registerSpecial(particle.get(), provider));
    }

    @Override
    public <T extends ParticleOptions> void register(Supplier<? extends ParticleType<T>> particle, ParticleFactory<T> factory) {
        NARAKA_BUS.addListener((Consumer<RegisterParticleProvidersEvent>) event -> event.registerSpriteSet(particle.get(), factory::create));
    }
}
