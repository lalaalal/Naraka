package com.yummy.naraka.core.particles;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public record SoulParticleOption(ParticleType<SoulParticleOption> type, SoulType soulType) implements ParticleOptions {
    public static SoulParticleOption with(SoulType soulType) {
        return new SoulParticleOption(NarakaParticleTypes.SOUL.get(), soulType);
    }

    public static ParticleType<SoulParticleOption> type(boolean force) {
        return new ParticleType<>(force) {
            @Override
            public MapCodec<SoulParticleOption> codec() {
                return SoulType.CODEC.xmap(
                        soulType -> new SoulParticleOption(this, soulType),
                        option -> option.soulType
                ).fieldOf("soul_type");
            }

            @Override
            public StreamCodec<? super RegistryFriendlyByteBuf, SoulParticleOption> streamCodec() {
                return SoulType.STREAM_CODEC.map(
                        soulType -> new SoulParticleOption(this, soulType),
                        option -> option.soulType
                );
            }
        };
    }

    @Override
    public ParticleType<?> getType() {
        return type;
    }
}
