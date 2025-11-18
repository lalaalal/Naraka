package com.yummy.naraka.world.entity;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.EntityDataSerializerRegistry;
import com.yummy.naraka.world.item.SoulType;
import net.minecraft.network.syncher.EntityDataSerializer;

public class NarakaEntityDataSerializers {
    public static final EntityDataSerializer<SoulType> SOUL_TYPE = EntityDataSerializer.forValueType(SoulType.STREAM_CODEC);

    public static void initialize() {
        EntityDataSerializerRegistry.register(NarakaMod.location("soul_type"), SOUL_TYPE);
    }
}
