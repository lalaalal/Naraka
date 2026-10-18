package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class NarakaPostEffectTypes {
    public static final HolderProxy<PostEffectType<?>, PostEffectType<ChromaticAberrationConfig>> CHROMATIC_ABERRATION = register(
            NarakaPostEffects.CHROMATIC_ABERRATION,
            ChromaticAberrationConfig.MAP_CODEC,
            ChromaticAberrationConfig.STREAM_CODEC,
            ChromaticAberrationConfig.class
    );

    public static final HolderProxy<PostEffectType<?>, PostEffectType<RyoikiTenkaiConfig>> RYOIKI_GREEN = register(
            NarakaPostEffects.RYOIKI_GREEN,
            RyoikiTenkaiConfig.MAP_CODEC,
            RyoikiTenkaiConfig.STREAM_CODEC,
            RyoikiTenkaiConfig.class
    );

    public static final HolderProxy<PostEffectType<?>, PostEffectType<RyoikiTenkaiConfig>> RYOIKI_GRAY = register(
            NarakaPostEffects.RYOIKI_GRAY,
            RyoikiTenkaiConfig.MAP_CODEC,
            RyoikiTenkaiConfig.STREAM_CODEC,
            RyoikiTenkaiConfig.class
    );

    private static <T extends PostEffectConfig> HolderProxy<PostEffectType<?>, PostEffectType<T>> register(Identifier id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, Class<T> type) {
        return RegistryProxy.register(NarakaRegistries.Keys.POST_EFFECT_TYPE, id, () -> new PostEffectType<>(id, codec, streamCodec, type));
    }

    public static void initialize() {

    }
}
