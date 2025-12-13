package com.yummy.naraka.world.entity.data;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.entity.Entity;

public record EntityData<T, E extends Entity>(EntityDataType<T, E> type, T value) {
    public static final StreamCodec<RegistryFriendlyByteBuf, EntityData<?, ?>> STREAM_CODEC = ByteBufCodecs.registry(NarakaRegistries.Keys.ENTITY_DATA_TYPE)
            .dispatch(EntityData::type, EntityDataType::streamCodec);

    public static final Codec<EntityData<?, ? extends Entity>> CODEC = NarakaRegistries.ENTITY_DATA_TYPE.byNameCodec()
            .dispatch(EntityData::type, EntityDataType::mapCodec);
}
