package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record EntityData<T>(EntityDataType<T> type, T value) {
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityData<?>> STREAM_CODEC = ByteBufCodecs.registry(NarakaRegistries.Keys.ENTITY_DATA_TYPE)
            .dispatch(EntityData::type, EntityDataType::streamCodec);

    public static final Codec<EntityData<?>> CODEC = NarakaRegistries.ENTITY_DATA_TYPE.byNameCodec()
            .dispatch(EntityData::type, EntityDataType::mapCodec);
}
