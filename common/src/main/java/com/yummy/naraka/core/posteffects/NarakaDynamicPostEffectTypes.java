package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryWriter;
import net.minecraft.resources.ResourceLocation;

import java.util.function.Supplier;

public class NarakaDynamicPostEffectTypes {
    public static final HolderProxy<DynamicPostEffectType<?>, DynamicPostEffectType<ChromaticAberrationConfig>> CHROMATIC_ABERRATION = register(
            NarakaPostEffects.CHROMATIC_ABERRATION,
            ChromaticAberrationConfig.MAP_CODEC,
            ChromaticAberrationConfig.class,
            () -> ChromaticAberrationConfig.DEFAULT
    );

    public static final HolderProxy<DynamicPostEffectType<?>, DynamicPostEffectType<RyoikiTenkaiConfig>> RYOIKI_GREEN = register(
            NarakaPostEffects.RYOIKI_GREEN,
            RyoikiTenkaiConfig.MAP_CODEC,
            RyoikiTenkaiConfig.class,
            () -> RyoikiTenkaiConfig.green(50, 10)
    );

    public static final HolderProxy<DynamicPostEffectType<?>, DynamicPostEffectType<RyoikiTenkaiConfig>> RYOIKI_GRAY = register(
            NarakaPostEffects.RYOIKI_GRAY,
            RyoikiTenkaiConfig.MAP_CODEC,
            RyoikiTenkaiConfig.class,
            () -> RyoikiTenkaiConfig.gray(50, 10)
    );

    private static <T extends PostEffectConfig> HolderProxy<DynamicPostEffectType<?>, DynamicPostEffectType<T>> register(ResourceLocation id, Codec<T> codec, Class<T> type, Supplier<T> defaultConfig) {
        return RegistryWriter.register(NarakaRegistries.Keys.DYNAMIC_POST_EFFECT_TYPE, id, () -> new DynamicPostEffectType<>(id, codec, type, defaultConfig));
    }

    public static void initialize() {

    }
}
