package com.yummy.naraka.core.registries;

import com.yummy.naraka.service.NarakaServices;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryFactory {
    static <T> RegistryReader<T> create(ResourceKey<Registry<T>> key) {
        return NarakaServices.REGISTRY_FACTORY.createSimple(key);
    }

    <T> RegistryReader<T> createSimple(ResourceKey<Registry<T>> key);
}