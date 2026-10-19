package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;

public class NarakaDynamicPostEffectTypes {
    public static final HolderProxy<DynamicPostEffectType<?>, DynamicPostEffectType<ChromaticAberrationConfig>> CHROMATIC_ABERRATION = register(
            NarakaPostEffects.CHROMATIC_ABERRATION,
            ChromaticAberrationConfig.MAP_CODEC,
            ChromaticAberrationConfig.STREAM_CODEC,
            ChromaticAberrationConfig.class,
            ChromaticAberrationConfig.DEFAULT
    );

    public static final HolderProxy<DynamicPostEffectType<?>, DynamicPostEffectType<RyoikiTenkaiConfig>> RYOIKI_GREEN = register(
            NarakaPostEffects.RYOIKI_GREEN,
            RyoikiTenkaiConfig.MAP_CODEC,
            RyoikiTenkaiConfig.STREAM_CODEC,
            RyoikiTenkaiConfig.class,
            RyoikiTenkaiConfig.green(50, 10)
    );

    public static final HolderProxy<DynamicPostEffectType<?>, DynamicPostEffectType<RyoikiTenkaiConfig>> RYOIKI_GRAY = register(
            NarakaPostEffects.RYOIKI_GRAY,
            RyoikiTenkaiConfig.MAP_CODEC,
            RyoikiTenkaiConfig.STREAM_CODEC,
            RyoikiTenkaiConfig.class,
            RyoikiTenkaiConfig.gray(50, 10)
    );

    private static <T extends PostEffectConfig> HolderProxy<DynamicPostEffectType<?>, DynamicPostEffectType<T>> register(Identifier id, MapCodec<T> codec, StreamCodec<? super RegistryFriendlyByteBuf, T> streamCodec, Class<T> type, T defaultConfig) {
        return RegistryProxy.register(NarakaRegistries.Keys.DYNAMIC_POST_EFFECT_TYPE, id, () -> new DynamicPostEffectType<>(id, codec, streamCodec, type, defaultConfig));
    }

    public static void initialize() {

    }
}
