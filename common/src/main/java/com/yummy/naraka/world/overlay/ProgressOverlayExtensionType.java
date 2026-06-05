package com.yummy.naraka.world.overlay;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

import java.util.Optional;

public class ProgressOverlayExtensionType<T> {
    private final Class<T> classType;
    private final MapCodec<ProgressOverlayData<T>> dataCodec;
    private final StreamCodec<RegistryFriendlyByteBuf, ProgressOverlayData<T>> dataStreamCodec;

    public ProgressOverlayExtensionType(Class<T> classType, Codec<T> codec) {
        this.classType = classType;
        this.dataCodec = RecordCodecBuilder.mapCodec(instance -> instance.group(
                        codec.fieldOf("value").forGetter(ProgressOverlayData::value)
                ).apply(instance, value -> new ProgressOverlayData<>(this, value))
        );
        this.dataStreamCodec = ByteBufCodecs.fromCodecWithRegistries(codec)
                .map(value -> new ProgressOverlayData<>(this, value), ProgressOverlayData::value);
    }

    public Optional<T> cast(Object value) {
        if (classType.isInstance(value))
            return Optional.of(classType.cast(value));
        return Optional.empty();
    }

    public ProgressOverlayData<T> createData(T value) {
        return new ProgressOverlayData<>(this, value);
    }

    public MapCodec<ProgressOverlayData<T>> dataCodec() {
        return dataCodec;
    }

    public StreamCodec<RegistryFriendlyByteBuf, ProgressOverlayData<T>> dataStreamCodec() {
        return dataStreamCodec;
    }
}
