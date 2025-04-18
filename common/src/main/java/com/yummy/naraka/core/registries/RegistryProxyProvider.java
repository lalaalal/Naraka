package com.yummy.naraka.core.registries;

import com.yummy.naraka.proxy.MethodInvoker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Singleton class provides {@link RegistryProxy}<br>
 * Override {@link RegistryProxyProvider#create(ResourceKey)} to create {@linkplain RegistryProxy} by default<br>
 */
public abstract class RegistryProxyProvider {
    @Nullable
    private static RegistryProxyProvider instance;

    private final Map<ResourceKey<? extends Registry<?>>, RegistryProxy<?>> registryProxyMap = new HashMap<>();

    protected static RegistryProxyProvider getInstance() {
        if (instance == null)
            instance = MethodInvoker.of(RegistryProxyProvider.class, "getInstance")
                    .invoke().result(RegistryProxyProvider.class);
        return instance;
    }

    public static void initialize() {
        getInstance();
    }

    public static <T> RegistryProxy<T> get(ResourceKey<Registry<T>> key) {
        return getInstance().getProxy(key);
    }

    protected RegistryProxyProvider() {
    }

    /**
     * Create default {@link RegistryProxy}<br>
     *
     * @param key Registry key
     * @param <T> Registry type
     * @return throws exception in this class
     */
    protected abstract <T> RegistryProxy<T> create(ResourceKey<Registry<T>> key);

    /**
     * Add {@link RegistryProxy}
     *
     * @param proxy New {@linkplain RegistryProxy}
     * @param <T>   Registry type
     * @return Self
     */
    public <T> RegistryProxyProvider add(RegistryProxy<T> proxy) {
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
