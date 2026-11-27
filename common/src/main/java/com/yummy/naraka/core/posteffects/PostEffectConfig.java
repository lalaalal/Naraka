package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface PostEffectConfig {
    Codec<PostEffectConfig> CODEC = DynamicPostEffectType.CODEC.dispatch(PostEffectConfig::type, DynamicPostEffectType::codec);
    StreamCodec<RegistryFriendlyByteBuf, PostEffectConfig> STREAM_CODEC = DynamicPostEffectType.STREAM_CODEC.dispatch(PostEffectConfig::type, DynamicPostEffectType::streamCodec);

    DynamicPostEffectType<?> type();
}
