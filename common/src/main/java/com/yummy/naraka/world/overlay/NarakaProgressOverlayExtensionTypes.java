package com.yummy.naraka.world.overlay;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;

public class NarakaProgressOverlayExtensionTypes {
    public static final HolderProxy<ProgressOverlayExtensionType<?>, ProgressOverlayExtensionType<Integer>> ORIGIN_HEROBRINE = register(
            "origin_herobrine", Integer.class, Codec.INT
    );

    private static <T> HolderProxy<ProgressOverlayExtensionType<?>, ProgressOverlayExtensionType<T>> register(String name, Class<T> classType, Codec<T> codec) {
        return RegistryProxy.register(
                NarakaRegistries.Keys.PROGRESS_OVERLAY_EXTENSION_TYPE,
                name, () -> new ProgressOverlayExtensionType<>(classType, codec)
        );
    }

    public static void initialize() {

    }
}
