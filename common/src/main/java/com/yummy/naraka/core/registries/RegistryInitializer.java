package com.yummy.naraka.core.registries;

import com.yummy.naraka.init.NarakaInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class provides {@link RegistryProxy}<br>
 * Override {@link RegistryInitializer#create(ResourceKey)} to create {@linkplain RegistryProxy} by default<br>
 */
public class RegistryInitializer {
    @Nullable
    private static RegistryInitializer INSTANCE;

    private final Map<ResourceKey<? extends Registry<?>>, RegistryProxy<?>> registryProxyMap = new HashMap<>();

    public static void initialize(NarakaInitializer initializer) {
        if (INSTANCE != null)
            throw new IllegalStateException("RegistryInitializer instance is already allocated");
        INSTANCE = initializer.getRegistryInitializer();
    }

    protected static RegistryInitializer getInstance() {
        if (INSTANCE == null)
            INSTANCE = new RegistryInitializer();
        return INSTANCE;
    }

    public static <T> RegistryProxy<T> get(ResourceKey<Registry<T>> key) {
        return getInstance().getProxy(key);
    }

    protected RegistryInitializer() {
    }

    /**
     * Create default {@link RegistryProxy}<br>
     *
     * @param key Registry key
     * @param <T> Registry type
     * @return throws exception in this class
     */
    protected <T> RegistryProxy<T> create(ResourceKey<Registry<T>> key) {
        throw new IllegalStateException("Creating RegistryProxy is not supported for default RegistryInitializer");
    }

    /**
     * Add {@link RegistryProxy}
     *
     * @param proxy New {@linkplain RegistryProxy}
     * @param <T>   Registry type
     * @return Self
     */
    public <T> RegistryInitializer add(RegistryProxy<T> proxy) {
        registryProxyMap.put(proxy.getRegistryKey(), proxy);
        return this;
    }

    private <T> RegistryProxy<T> addAndReturn(RegistryProxy<T> proxy) {
        add(proxy);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    public <T> RegistryProxy<T> getProxy(ResourceKey<Registry<T>> key) {
        RegistryProxy<?> proxy = registryProxyMap.get(key);
        if (proxy != null)
            return (RegistryProxy<T>) proxy;
        return addAndReturn(create(key));
    }
}
