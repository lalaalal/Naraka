package com.yummy.naraka.world.overlay;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;

public record ProgressOverlayData<T>(ProgressOverlayExtensionType<T> type, T value) {
    public static final StreamCodec<RegistryFriendlyByteBuf, ProgressOverlayData<?>> STREAM_CODEC = ByteBufCodecs.registry(NarakaRegistries.Keys.PROGRESS_OVERLAY_EXTENSION_TYPE)
            .dispatch(ProgressOverlayData::type, ProgressOverlayExtensionType::dataStreamCodec);

    public static final Codec<ProgressOverlayData<?>> CODEC = NarakaRegistries.PROGRESS_OVERLAY_EXTENSION_TYPE.byNameCodec()
            .dispatch(ProgressOverlayData::type, ProgressOverlayExtensionType::dataCodec);
}
