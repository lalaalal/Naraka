package com.yummy.naraka.core.particles;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class NarakaParticleTypes {
    public static final SimpleParticleType EBONY_LEAVES = register("ebony_leaves", false);
    public static final SimpleParticleType DRIPPING_NECTARIUM_CORE_HONEY = register("dripping_nectarium_core_honey", false);
    public static final SimpleParticleType FALLING_NECTARIUM_CORE_HONEY = register("falling_nectarium_core_honey", false);
    public static final SimpleParticleType LANDING_NECTARIUM_CORE_HONEY = register("landing_nectarium_core_honey", false);

    private static SimpleParticleType register(String name, boolean alwaysSpawn) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, NarakaMod.location(name), FabricParticleTypes.simple(alwaysSpawn));
    }

    public static void initialize() {

    }
}

