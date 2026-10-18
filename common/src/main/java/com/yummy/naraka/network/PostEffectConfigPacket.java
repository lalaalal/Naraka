package com.yummy.naraka.network;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.posteffects.PostEffectConfig;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;

public record PostEffectConfigPacket(PostEffectConfig config) implements CustomPacketPayload {
    public static final Type<PostEffectConfigPacket> TYPE = new Type<>(NarakaMod.identifier("post_effect_config"));

    public static final StreamCodec<RegistryFriendlyByteBuf, PostEffectConfigPacket> CODEC = PostEffectConfig.STREAM_CODEC.map(PostEffectConfigPacket::new, PostEffectConfigPacket::config);

    @Override
    public Type<PostEffectConfigPacket> type() {
        return TYPE;
    }
}
