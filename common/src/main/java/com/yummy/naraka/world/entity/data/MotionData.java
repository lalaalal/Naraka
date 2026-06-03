package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionfc;

import java.util.List;

public record MotionData(Identifier id, List<Vec3> positions, List<Quaternionfc> rotations) {
    public static final Codec<MotionData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    Identifier.CODEC.fieldOf("id").forGetter(MotionData::id),
                    Vec3.CODEC.listOf().fieldOf("positions").forGetter(MotionData::positions),
                    ExtraCodecs.QUATERNIONF.listOf().fieldOf("rotations").forGetter(MotionData::rotations)
            ).apply(instance, MotionData::new)
    );

    public static final StreamCodec<ByteBuf, MotionData> STREAM_CODEC = StreamCodec.composite(
            Identifier.STREAM_CODEC,
            MotionData::id,
            Vec3.STREAM_CODEC.apply(ByteBufCodecs.list()),
            MotionData::positions,
            ByteBufCodecs.QUATERNIONF.apply(ByteBufCodecs.list()),
            MotionData::rotations,
            MotionData::new
    );

    public static final MotionData DEFAULT = new MotionData(NarakaMod.identifier("default"), List.of(), List.of());
}
