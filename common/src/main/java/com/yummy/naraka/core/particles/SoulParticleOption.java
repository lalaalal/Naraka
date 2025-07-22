package com.yummy.naraka.core.particles;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.world.item.SoulType;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.codec.StreamCodec;

public record SoulParticleOption(SoulType soulType) implements ParticleOptions {
    public static final MapCodec<SoulParticleOption> MAP_CODEC = SoulType.CODEC.xmap(
            SoulParticleOption::new,
            option -> option.soulType
    ).fieldOf("soul_type");

    public static final StreamCodec<ByteBuf, SoulParticleOption> STREAM_CODEC = SoulType.STREAM_CODEC.map(
            SoulParticleOption::new,
            option -> option.soulType
    );

    public static SoulParticleOption with(SoulType soulType) {
        return new SoulParticleOption(soulType);
    }

    public static ParticleType<SoulParticleOption> type(boolean force) {
        return SimpleCodecParticleType.of(force, MAP_CODEC, STREAM_CODEC);
    }

    @Override
    public ParticleType<?> getType() {
        return NarakaParticleTypes.SOUL.get();
    }
}
