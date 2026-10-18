package com.yummy.naraka.network.packet;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.FloatInterpolation;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.IdMapper;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Util;

public record SoundControlPacket(SoundSource source, float targetVolume, int duration,
                                 FloatInterpolation interpolation) implements CustomPacketPayload {
    public static final Type<SoundControlPacket> TYPE = new Type<>(NarakaMod.identifier("sound_control"));

    private static final StreamCodec<ByteBuf, SoundSource> SOUND_SOURCE_STREAM_CODEC = ByteBufCodecs.idMapper(
            Util.make(new IdMapper<>(), mapper -> {
                for (SoundSource value : SoundSource.values())
                    mapper.add(value);
            })
    );

    public static final StreamCodec<ByteBuf, SoundControlPacket> CODEC = StreamCodec.composite(
            SOUND_SOURCE_STREAM_CODEC,
            SoundControlPacket::source,
            ByteBufCodecs.FLOAT,
            SoundControlPacket::targetVolume,
            ByteBufCodecs.INT,
            SoundControlPacket::duration,
            FloatInterpolation.STREAM_CODEC,
            SoundControlPacket::interpolation,
            SoundControlPacket::new
    );

    public static SoundControlPacket increase(SoundSource source, int duration, FloatInterpolation interpolation) {
        return new SoundControlPacket(source, 1, duration, interpolation);
    }

    public static SoundControlPacket decrease(SoundSource source, int duration, FloatInterpolation interpolation) {
        return new SoundControlPacket(source, 0, duration, interpolation);
    }

    @Override
    public Type<SoundControlPacket> type() {
        return TYPE;
    }

    public float interpolate(float delta, float startVolume) {
        return interpolation.interpolate(delta, startVolume, targetVolume);
    }
}
