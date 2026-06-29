package com.yummy.naraka.world.overlay;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.NarakaRegistries;

public record ProgressOverlayData<T>(ProgressOverlayExtensionType<T> type, T value) {
    public static final Codec<ProgressOverlayData<?>> CODEC = NarakaRegistries.PROGRESS_OVERLAY_EXTENSION_TYPE.codec()
            .dispatch(ProgressOverlayData::type, ProgressOverlayExtensionType::dataCodec);
}
