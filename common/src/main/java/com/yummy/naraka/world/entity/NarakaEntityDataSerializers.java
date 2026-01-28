package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.EntityDataSerializerRegistry;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;

public class NarakaEntityDataSerializers {
    public static final EntityDataSerializer<SoulType> SOUL_TYPE = EntityDataSerializer.forValueType(SoulType.STREAM_CODEC);
    public static final EntityDataSerializer<QuadraticBezier> BEZIER = EntityDataSerializer.forValueType(QuadraticBezier.STREAM_CODEC);
    public static final EntityDataSerializer<Vec3> VEC3 = EntityDataSerializer.forValueType(ByteBufCodecs.fromCodec(Vec3.CODEC));

    public static void initialize() {
        EntityDataSerializerRegistry.register(NarakaMod.location("soul_type"), SOUL_TYPE);
        EntityDataSerializerRegistry.register(NarakaMod.location("bezier"), BEZIER);
        EntityDataSerializerRegistry.register(NarakaMod.location("vec3"), VEC3);
    }
}
