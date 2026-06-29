package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.world.entity.Entity;

public record EntityData<T, E extends Entity>(EntityDataType<T, E> type, T value) {
    public static final Codec<EntityData<?, ? extends Entity>> CODEC = NarakaRegistries.ENTITY_DATA_TYPE.codec()
            .dispatch(EntityData::type, EntityDataType::codec);
}
