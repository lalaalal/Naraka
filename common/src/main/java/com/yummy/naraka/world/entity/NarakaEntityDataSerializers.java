package com.yummy.naraka.world.entity;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.EntityDataSerializerRegistry;
import com.yummy.naraka.util.QuadraticBezier;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class NarakaEntityDataSerializers {
    public static final EntityDataSerializer<SoulType> SOUL_TYPE = EntityDataSerializer.simpleEnum(SoulType.class);
    public static final EntityDataSerializer<List<SoulType>> SOUL_TYPES = fromCodec(SoulType.CODEC.listOf());
    public static final EntityDataSerializer<QuadraticBezier> BEZIER = fromCodec(QuadraticBezier.CODEC);
    public static final EntityDataSerializer<Vec3> VEC3 = fromCodec(Vec3.CODEC);
    public static final EntityDataSerializer<AreaShape> AREA_SHAPE = EntityDataSerializer.simpleEnum(AreaShape.class);

    private static <T> EntityDataSerializer<T> fromCodec(final Codec<T> codec) {
        FriendlyByteBuf.Writer<T> writer = (buffer, data) -> buffer.writeJsonWithCodec(codec, data);
        FriendlyByteBuf.Reader<T> reader = buffer -> buffer.readJsonWithCodec(codec);
        return EntityDataSerializer.simple(writer, reader);
    }

    public static void initialize() {
        EntityDataSerializerRegistry.register(NarakaMod.location("soul_type"), SOUL_TYPE);
        EntityDataSerializerRegistry.register(NarakaMod.location("soul_types"), SOUL_TYPES);
        EntityDataSerializerRegistry.register(NarakaMod.location("bezier"), BEZIER);
        EntityDataSerializerRegistry.register(NarakaMod.location("vec3"), VEC3);
        EntityDataSerializerRegistry.register(NarakaMod.location("area_shape"), AREA_SHAPE);
    }
}
