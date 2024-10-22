package com.yummy.naraka.core.particles;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

public class NarakaParticleTypes {
    private static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.PARTICLE_TYPE);

    public static final RegistrySupplier<SimpleParticleType> EBONY_LEAVES = register("ebony_leaves", false);

    private static RegistrySupplier<SimpleParticleType> register(String name, boolean alwaysSpawn) {
        return PARTICLE_TYPES.register(name, () -> new NarakaSimpleParticleType(alwaysSpawn));
    }

    public static void initialize() {
        PARTICLE_TYPES.register();
    }

    private static class NarakaSimpleParticleType extends SimpleParticleType {
        public NarakaSimpleParticleType(boolean overrideLimiter) {
            super(overrideLimiter);
        }
    }
}

