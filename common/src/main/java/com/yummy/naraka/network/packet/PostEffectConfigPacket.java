package com.yummy.naraka.network.packet;

import com.mojang.serialization.Codec;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.posteffects.PostEffectConfig;
import com.yummy.naraka.network.CustomPacketPayload;

public record PostEffectConfigPacket(PostEffectConfig config) implements CustomPacketPayload<PostEffectConfigPacket> {
    public static final Codec<PostEffectConfigPacket> CODEC = PostEffectConfig.CODEC.xmap(PostEffectConfigPacket::new, PostEffectConfigPacket::config);

    public static final Type<PostEffectConfigPacket> TYPE = new RegistryCodecType<>(NarakaMod.location("post_effect_config"),
            PostEffectConfigPacket.class,
            CODEC
    );

    @Override
    public Type<PostEffectConfigPacket> type() {
        return TYPE;
    }
}
