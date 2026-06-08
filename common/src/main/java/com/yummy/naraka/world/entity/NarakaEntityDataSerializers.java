package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.EntityDataSerializerRegistry;
import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NarakaEntityDataSerializers {
    public static final EntityDataSerializer<SoulType> SOUL_TYPE = EntityDataSerializer.forValueType(SoulType.STREAM_CODEC);
    public static final EntityDataSerializer<List<SoulType>> SOUL_TYPES = EntityDataSerializer.forValueType(SoulType.STREAM_CODEC.apply(ByteBufCodecs.list()));
    public static final EntityDataSerializer<QuadraticBezier> BEZIER = EntityDataSerializer.forValueType(QuadraticBezier.STREAM_CODEC);
    public static final EntityDataSerializer<Vec3> VEC3 = EntityDataSerializer.forValueType(ByteBufCodecs.fromCodec(Vec3.CODEC));
    public static final EntityDataSerializer<AreaShape> AREA_SHAPE = EntityDataSerializer.forValueType(AreaShape.STREAM_CODEC);


    public static void initialize() {
        EntityDataSerializerRegistry.register(NarakaMod.location("soul_type"), SOUL_TYPE);
        EntityDataSerializerRegistry.register(NarakaMod.location("soul_types"), SOUL_TYPES);
        EntityDataSerializerRegistry.register(NarakaMod.location("bezier"), BEZIER);
        EntityDataSerializerRegistry.register(NarakaMod.location("vec3"), VEC3);
        EntityDataSerializerRegistry.register(NarakaMod.location("area_shape"), AREA_SHAPE);

    }
}
