package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;

public interface PostEffectConfig {
    Codec<PostEffectConfig> CODEC = PostEffectType.CODEC.dispatch(PostEffectConfig::type, PostEffectType::layoutCodec);
    StreamCodec<RegistryFriendlyByteBuf, PostEffectConfig> STREAM_CODEC = PostEffectType.STREAM_CODEC.dispatch(PostEffectConfig::type, PostEffectType::streamCodec);

    PostEffectType<?> type();
}
