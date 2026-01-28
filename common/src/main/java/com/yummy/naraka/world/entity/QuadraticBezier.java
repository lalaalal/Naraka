package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.util.NarakaUtils;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.phys.Vec3;

public record QuadraticBezier(Vec3 v1, Vec3 v2, Vec3 v3) {
    public static final QuadraticBezier ZERO = new QuadraticBezier(Vec3.ZERO, Vec3.ZERO, Vec3.ZERO);

    public static final Codec<QuadraticBezier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Vec3.CODEC.fieldOf("v1").forGetter(QuadraticBezier::v1),
            Vec3.CODEC.fieldOf("v2").forGetter(QuadraticBezier::v2),
            Vec3.CODEC.fieldOf("v3").forGetter(QuadraticBezier::v3)
    ).apply(instance, QuadraticBezier::new));

    private static final StreamCodec<ByteBuf, Vec3> VEC3_STREAM_CODEC = ByteBufCodecs.fromCodec(Vec3.CODEC);
    public static final StreamCodec<ByteBuf, QuadraticBezier> STREAM_CODEC = StreamCodec.composite(
            VEC3_STREAM_CODEC,
            QuadraticBezier::v1,
            VEC3_STREAM_CODEC,
            QuadraticBezier::v2,
            VEC3_STREAM_CODEC,
            QuadraticBezier::v3,
            QuadraticBezier::new
    );

    public static QuadraticBezier fromZero(Vec3 v2, Vec3 v3) {
        return new QuadraticBezier(Vec3.ZERO, v2, v3);
    }

    public QuadraticBezier rotated(float yRot) {
        return new QuadraticBezier(
                v1.yRot(yRot),
                v2.yRot(yRot),
                v3.yRot(yRot)
        );
    }

    public Vec3 interpolate(float delta) {
        return NarakaUtils.quadraticBezier(delta, v1, v2, v3);
    }
}
