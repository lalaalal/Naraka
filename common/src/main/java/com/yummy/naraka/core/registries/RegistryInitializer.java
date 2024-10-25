package com.yummy.naraka.core.registries;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;

public class RegistryInitializer {
    public static final RegistryInitializer INSTANCE = new RegistryInitializer();

    private final Map<ResourceKey<? extends Registry<?>>, RegistryProxy<?>> registryProxyMap = new HashMap<>();

    private RegistryInitializer() {
    }

    public <T> RegistryInitializer add(RegistryProxy<T> proxy) {
        registryProxyMap.put(proxy.getRegistryKey(), proxy);
        return this;
    }

    @SuppressWarnings("unchecked")
    public static <T> RegistryProxy<T> get(ResourceKey<Registry<T>> key) {
        RegistryProxy<?> proxy = INSTANCE.registryProxyMap.get(key);
        return (RegistryProxy<T>) proxy;
    }
}
