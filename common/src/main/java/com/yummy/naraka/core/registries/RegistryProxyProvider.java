package com.yummy.naraka.core.registries;

import com.yummy.naraka.service.NarakaServices;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Singleton class provides {@link RegistryWriter}<br>
 */
public abstract class RegistryProxyProvider {
    private final Map<ResourceKey<? extends Registry<?>>, RegistryWriter<?>> registryWriterMap = new HashMap<>();
    private final Map<ResourceKey<? extends Registry<?>>, RegistryReader<?>> registryReaderMap = new HashMap<>();

    public static <T> RegistryWriter<T> get(ResourceKey<Registry<T>> key) {
        return NarakaServices.REGISTRY_PROXY_PROVIDER.getProxy(key);
    }

    public static void forEach(Consumer<RegistryWriter<?>> consumer) {
        NarakaServices.REGISTRY_PROXY_PROVIDER.registryWriterMap.values().forEach(consumer);
    }

    protected RegistryProxyProvider() {
    }

    /**
     * Create default {@link RegistryWriter}<br>
     *
     * @param key Registry key
     * @param <T> Registry type
     */
    protected abstract <T> RegistryWriter<T> create(ResourceKey<Registry<T>> key);

    /**
     * Add {@link RegistryWriter}
     *
     * @param proxy New {@linkplain RegistryWriter}
     * @param <T>   Registry type
     * @return Self
     */
    public <T> RegistryProxyProvider add(RegistryWriter<T> proxy) {
        registryWriterMap.put(proxy.getRegistryKey(), proxy);
        return this;
    }

    private <T> RegistryWriter<T> addAndReturn(RegistryWriter<T> proxy) {
        add(proxy);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    public <T> RegistryWriter<T> getProxy(ResourceKey<Registry<T>> key) {
        RegistryWriter<?> proxy = registryWriterMap.get(key);
        if (proxy != null)
            return (RegistryWriter<T>) proxy;
        return addAndReturn(create(key));
    }

    @SuppressWarnings("unchecked")
    public <T> Optional<RegistryReader<T>> getRegistryReader(ResourceKey<Registry<T>> key) {
        if (registryReaderMap.containsKey(key))
            return Optional.of((RegistryReader<T>) registryReaderMap.get(key));
        Registry<T> registry = (Registry<T>) BuiltInRegistries.REGISTRY.get(key.location());
        if (registry == null)
            return Optional.empty();
        RegistryReader<T> reader = new RegistryReader.Simple<>(registry);
        registryReaderMap.put(key, reader);
        return Optional.of(reader);
    }
}
