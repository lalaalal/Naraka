package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.NarakaMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

import java.util.List;

public record MotionData(ResourceLocation id, List<Vec3> positions, List<Quaternionf> rotations) {
    public static final Codec<MotionData> CODEC = RecordCodecBuilder.create(
            instance -> instance.group(
                    ResourceLocation.CODEC.fieldOf("id").forGetter(MotionData::id),
                    Vec3.CODEC.listOf().fieldOf("positions").forGetter(MotionData::positions),
                    ExtraCodecs.QUATERNIONF.listOf().fieldOf("rotations").forGetter(MotionData::rotations)
            ).apply(instance, MotionData::new)
    );

    public static final MotionData DEFAULT = new MotionData(NarakaMod.location("default"), List.of(), List.of());
}
