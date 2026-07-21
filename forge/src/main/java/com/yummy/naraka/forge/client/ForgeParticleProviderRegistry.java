package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.ParticleProviderRegistry;
import com.yummy.naraka.client.particle.ParticleFactory;
import com.yummy.naraka.core.registries.ValueGetter;
import com.yummy.naraka.forge.NarakaEventBus;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;

import java.util.function.Consumer;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class ForgeParticleProviderRegistry implements NarakaEventBus {
    @MethodProxy(ParticleProviderRegistry.class)
    public static <T extends ParticleOptions> void register(ValueGetter<? extends ParticleType<T>> particle, ParticleProvider<T> provider) {
        NARAKA_BUS.addListener((Consumer<RegisterParticleProvidersEvent>) event -> event.registerSpecial(particle.getConcreteValue(), provider));
    }

    @MethodProxy(ParticleProviderRegistry.class)
    public static <T extends ParticleOptions> void register(ValueGetter<? extends ParticleType<T>> particle, ParticleFactory<T> factory) {
        NARAKA_BUS.addListener((Consumer<RegisterParticleProvidersEvent>) event -> event.registerSpriteSet(particle.getConcreteValue(), factory::create));
    }
}
