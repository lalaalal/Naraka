package com.yummy.naraka.init;

import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

public class RegistryInitializer {
    private static @Nullable RegistryInitializer INSTANCE;

    private final Map<ResourceKey<? extends Registry<?>>, RegistryProxy<?>> registryProxyMap = new HashMap<>();

    public static void allocateInstance(RegistryInitializer instance) {
        if (INSTANCE != null)
            throw new IllegalStateException("RegistryInitializer instance is already allocated");
        INSTANCE = instance;
    }

    public static RegistryInitializer getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RegistryInitializer();
        return INSTANCE;
    }

    public static <T> RegistryProxy<T> get(ResourceKey<Registry<T>> key) {
        return getInstance().getProxy(key);
    }

    protected RegistryInitializer() {
    }

    protected <T> RegistryProxy<T> create(ResourceKey<Registry<T>> key) {
        throw new IllegalStateException("Creating RegistryProxy is not supported for default RegistryInitializer");
    }

    public <T> RegistryInitializer add(RegistryProxy<T> proxy) {
        registryProxyMap.put(proxy.getRegistryKey(), proxy);
        return this;
    }

    protected <T> RegistryProxy<T> register(RegistryProxy<T> proxy) {
        add(proxy);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    public <T> RegistryProxy<T> getProxy(ResourceKey<Registry<T>> key) {
        RegistryProxy<?> proxy = registryProxyMap.get(key);
        if (proxy != null)
            return (RegistryProxy<T>) proxy;
        return register(create(key));
    }
}
