package com.yummy.naraka.core.particles;

import com.mojang.serialization.MapCodec;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public class SimpleCodecParticleType<T extends ParticleOptions> extends ParticleType<T> {
    private final MapCodec<T> codec;
    private final StreamCodec<ByteBuf, T> streamCodec;

    public static <T extends ParticleOptions> SimpleCodecParticleType<T> of(boolean force, MapCodec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {
        return new SimpleCodecParticleType<>(force, codec, streamCodec);
    }

    protected SimpleCodecParticleType(boolean force, MapCodec<T> codec, StreamCodec<ByteBuf, T> streamCodec) {
        super(force);
        this.codec = codec;
        this.streamCodec = streamCodec;
    }

    @Override
    public MapCodec<T> codec() {
        return codec;
    }

    @Override
    public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }
}
