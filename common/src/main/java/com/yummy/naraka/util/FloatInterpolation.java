package com.yummy.naraka.util;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum FloatInterpolation implements Interpolation<Float>, StringRepresentable {
    LINEAR(Interpolation.F_LINEAR),
    FAST_STEP_IN(Interpolation.F_FAST_STEP_IN),
    FAST_STEP_OUT(Interpolation.F_FAST_STEP_OUT);

    public static final Codec<FloatInterpolation> CODEC = StringRepresentable.fromEnum(FloatInterpolation::values);
    public static final StreamCodec<ByteBuf, FloatInterpolation> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    private final Interpolation<Float> interpolation;

    FloatInterpolation(Interpolation<Float> interpolation) {
        this.interpolation = interpolation;
    }

    @Override
    public Float interpolate(float delta, Float from, Float to) {
        return interpolation.interpolate(delta, from, to);
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase();
    }
}
