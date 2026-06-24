package com.yummy.naraka.world.overlay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.Optional;

public class ProgressOverlayExtensionType<T> {
    private final Class<T> classType;
    private final Codec<ProgressOverlayData<T>> dataCodec;

    public ProgressOverlayExtensionType(Class<T> classType, Codec<T> codec) {
        this.classType = classType;
        this.dataCodec = RecordCodecBuilder.create(instance -> instance.group(
                        codec.fieldOf("value").forGetter(ProgressOverlayData::value)
                ).apply(instance, value -> new ProgressOverlayData<>(this, value))
        );
    }

    public Optional<T> cast(Object value) {
        if (classType.isInstance(value))
            return Optional.of(classType.cast(value));
        return Optional.empty();
    }

    public ProgressOverlayData<T> createData(T value) {
        return new ProgressOverlayData<>(this, value);
    }

    public Codec<ProgressOverlayData<T>> dataCodec() {
        return dataCodec;
    }
}
