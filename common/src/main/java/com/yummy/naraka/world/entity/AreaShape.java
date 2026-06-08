package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.StringRepresentable;

public enum AreaShape implements StringRepresentable {
    RECTANGLE,
    CIRCLE;

    public static final Codec<AreaShape> CODEC = StringRepresentable.fromValues(AreaShape::values);
    public static final StreamCodec<ByteBuf, AreaShape> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    @Override
    public String getSerializedName() {
        return name();
    }
}
