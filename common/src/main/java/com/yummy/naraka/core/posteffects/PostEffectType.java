package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

import java.util.Optional;

public class PostEffectType<T extends PostEffectConfig> {
    public static final Codec<PostEffectType<?>> CODEC = NarakaRegistries.POST_EFFECT_TYPE.byNameCodec();
    public static final StreamCodec<RegistryFriendlyByteBuf, PostEffectType<?>> STREAM_CODEC = ByteBufCodecs.registry(NarakaRegistries.Keys.POST_EFFECT_TYPE);

    private final Identifier id;
    private final MapCodec<T> codec;
    private final StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec;
    private final Class<T> classType;

    public static Optional<PostEffectType<?>> fromId(Identifier id) {
        return NarakaRegistries.POST_EFFECT_TYPE.getOptional(id);
    }

    public PostEffectType(Identifier id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, Class<T> classType) {
        this.id = id;
        this.codec = codec;
        this.streamCodec = streamCodec;
        this.classType = classType;
    }

    public Identifier id() {
        return id;
    }

    public MapCodec<T> codec() {
        return codec;
    }

    public StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }

    public T cast(PostEffectConfig config) {
        if (config.type() == this && classType.isInstance(config))
            return classType.cast(config);
        throw new IllegalStateException("Type mismatch");
    }
}
