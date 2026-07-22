package com.yummy.naraka.core.registries;

import com.yummy.naraka.service.NarakaServices;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

public interface RegistryFactory {
    static <T> Registry<T> create(ResourceKey<Registry<T>> key) {
        return NarakaServices.REGISTRY_FACTORY.createSimple(key);
    }

    <T> Registry<T> createSimple(ResourceKey<Registry<T>> key);
}