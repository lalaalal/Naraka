package com.yummy.naraka.network;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.util.FloatInterpolation;
import com.yummy.naraka.util.NarakaExtraCodecs;
import net.minecraft.sounds.SoundSource;

public record SoundControlPacket(SoundSource source, float targetVolume, int duration,
                                 FloatInterpolation interpolation) implements CustomPacketPayload<SoundControlPacket> {

    public static final Codec<SoundControlPacket> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    NarakaExtraCodecs.SOUND_SOURCE.fieldOf("source").forGetter(SoundControlPacket::source),
                    Codec.FLOAT.fieldOf("target_volume").forGetter(SoundControlPacket::targetVolume),
                    Codec.INT.fieldOf("duration").forGetter(SoundControlPacket::duration),
                    FloatInterpolation.CODEC.fieldOf("interpolation").forGetter(SoundControlPacket::interpolation)
            ).apply(instance, SoundControlPacket::new)
    );

    public static final Type<SoundControlPacket> TYPE = new CodecType<>(NarakaMod.location("sound_control"),
            SoundControlPacket.class,
            CODEC
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
