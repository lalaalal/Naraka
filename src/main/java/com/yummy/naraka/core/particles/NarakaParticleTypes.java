package com.yummy.naraka.core.particles;

import com.yummy.naraka.NarakaMod;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;

public class NarakaParticleTypes {
    public static final SimpleParticleType EBONY_LEAVES = register("ebony_leaves");

    private static SimpleParticleType register(String name) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, NarakaMod.location(name), FabricParticleTypes.simple());
    }

    public static void initialize() {

    }
}
