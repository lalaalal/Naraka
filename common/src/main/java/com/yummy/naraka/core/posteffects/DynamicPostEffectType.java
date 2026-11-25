package com.yummy.naraka.core.posteffects;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.function.Supplier;

public class DynamicPostEffectType<T extends PostEffectConfig> {
    public static final Codec<DynamicPostEffectType<?>> CODEC = NarakaRegistries.DYNAMIC_POST_EFFECT_TYPE.codec();

    private final ResourceLocation id;
    private final Codec<T> codec;
    private final Class<T> classType;
    private final Supplier<T> defaultConfig;

    public static Optional<DynamicPostEffectType<?>> fromId(ResourceLocation id) {
        return NarakaRegistries.DYNAMIC_POST_EFFECT_TYPE.getHolder(id).map(Holder::value);
    }

    public DynamicPostEffectType(ResourceLocation id, Codec<T> codec, Class<T> classType, Supplier<T> defaultConfig) {
        this.id = id;
        this.codec = codec;
        this.classType = classType;
        this.defaultConfig = defaultConfig;
    }

    public ResourceLocation id() {
        return id;
    }

    public Codec<T> codec() {
        return codec;
    }

    public T defaultConfig() {
        return defaultConfig.get();
    }

    public T cast(PostEffectConfig config) {
        if (config.type() == this && classType.isInstance(config))
            return classType.cast(config);
        throw new IllegalStateException("Type mismatch");
    }
}
