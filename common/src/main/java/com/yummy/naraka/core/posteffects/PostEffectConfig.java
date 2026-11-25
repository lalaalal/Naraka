package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;

public interface PostEffectConfig {
    Codec<PostEffectConfig> CODEC = DynamicPostEffectType.CODEC.dispatch(PostEffectConfig::type, DynamicPostEffectType::codec);

    DynamicPostEffectType<?> type();
}
