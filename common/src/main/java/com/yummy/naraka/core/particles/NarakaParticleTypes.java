package com.yummy.naraka.core.particles;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.Registries;

import java.util.function.Function;

public class NarakaParticleTypes {
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> EBONY_LEAVES = register("ebony_leaves", false);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> DRIPPING_NECTARIUM = register("dripping_nectarium", false);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> FALLING_NECTARIUM = register("falling_nectarium", false);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> LANDING_NECTARIUM = register("landing_nectarium", false);

    public static final HolderProxy<ParticleType<?>, ParticleType<SoulParticleOption>> SOUL = register("soul", false, SoulParticleOption::type);

    public static final HolderProxy<ParticleType<?>, SimpleParticleType> GOLDEN_FLAME = register("golden_flame", false);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> STARDUST_FLAME = register("stardust_flame", false);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> HEROBRINE_SPAWN = register("herobrine_spawn", false);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> CORRUPTED_FIRE_FLAME = register("corrupted_fire_flame", false);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> CORRUPTED_SOUL_FIRE_FLAME = register("corrupted_soul_fire_flame", false);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> FLICKER = register("flicker", true);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> TELEPORT = register("teleport", true);
    public static final HolderProxy<ParticleType<?>, SimpleParticleType> STARDUST = register("stardust", true);

    private static <T extends ParticleType<?>> HolderProxy<ParticleType<?>, T> register(String name, boolean force, Function<Boolean, T> factory) {
        return RegistryProxy.register(Registries.PARTICLE_TYPE, name, () -> factory.apply(force));
    }

    private static HolderProxy<ParticleType<?>, SimpleParticleType> register(String name, boolean force) {
        return RegistryProxy.register(Registries.PARTICLE_TYPE, name, () -> new NarakaSimpleParticleType(force));
    }

    public static void initialize() {

    }

    private static class NarakaSimpleParticleType extends SimpleParticleType {
        public NarakaSimpleParticleType(boolean force) {
            super(force);
        }
    }
}

