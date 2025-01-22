package com.yummy.naraka.world.entity;

import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.network.syncher.EntityDataSerializers;

import java.util.ArrayList;
import java.util.List;

public class NarakaEntityDataSerializers {
    public static final StreamCodec<ByteBuf, List<Afterimage>> AFTERIMAGES_CODEC = ByteBufCodecs.collection(ArrayList::new, Afterimage.STREAM_CODEC);

    public static final EntityDataSerializer<List<Afterimage>> AFTERIMAGES = EntityDataSerializer.forValueType(AFTERIMAGES_CODEC);

    public static void initialize() {
        EntityDataSerializers.registerSerializer(AFTERIMAGES);
    }
}
