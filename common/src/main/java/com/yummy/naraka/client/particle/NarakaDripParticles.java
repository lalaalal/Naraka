package com.yummy.naraka.client.particle;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;

import java.util.Map;

@Environment(EnvType.CLIENT)
public class NarakaDripParticles {
    public static final Map<ParticleType<?>, ParticleOptions> REMAP_AFTER_HANG = Map.of(
            NarakaParticleTypes.DRIPPING_NECTARIUM.get(), NarakaParticleTypes.FALLING_NECTARIUM.get()
    );
    public static final Map<ParticleType<?>, ParticleOptions> REMAP_AFTER_FALL = Map.of(
            NarakaParticleTypes.FALLING_NECTARIUM.get(), NarakaParticleTypes.LANDING_NECTARIUM.get()
    );
}
