package com.yummy.naraka.core.registries;

import com.yummy.naraka.init.NarakaInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

public abstract class RegistryFactory {
    @Nullable
    private static RegistryFactory instance = null;

    @Nullable
    public static RegistryFactory getInstance() {
        return instance;
    }

    public static void initialize(NarakaInitializer initializer) {
        instance = initializer.getRegistryFactory();
    }

    public abstract <T> Registry<T> createSimple(ResourceKey<Registry<T>> key);
}