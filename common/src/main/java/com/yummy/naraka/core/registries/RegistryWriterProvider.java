package com.yummy.naraka.core.registries;

import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

/**
 * Singleton class provides {@link RegistryWriter}<br>
 */
public abstract class RegistryWriterProvider {
    @Nullable
    private static RegistryWriterProvider instance;

    private final Map<ResourceKey<? extends Registry<?>>, RegistryWriter<?>> registryProxyMap = new HashMap<>();

    protected static RegistryWriterProvider getInstance() {
        if (instance == null)
            instance = MethodInvoker.of(RegistryWriterProvider.class, "getInstance")
                    .invoke().result(RegistryWriterProvider.class);
        return instance;
    }

    public static void initialize() {
        getInstance();
    }

    public static <T> RegistryWriter<T> get(ResourceKey<Registry<T>> key) {
        return getInstance().getProxy(key);
    }

    public static void forEach(Consumer<RegistryWriter<?>> consumer) {
        getInstance().registryProxyMap.values().forEach(consumer);
    }

    protected RegistryWriterProvider() {
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
    public <T> RegistryWriterProvider add(RegistryWriter<T> proxy) {
        registryProxyMap.put(proxy.getRegistryKey(), proxy);
        return this;
    }

    private <T> RegistryWriter<T> addAndReturn(RegistryWriter<T> proxy) {
        add(proxy);
        return proxy;
    }

    @SuppressWarnings("unchecked")
    public <T> RegistryWriter<T> getProxy(ResourceKey<Registry<T>> key) {
        RegistryWriter<?> proxy = registryProxyMap.get(key);
        if (proxy != null)
            return (RegistryWriter<T>) proxy;
        return addAndReturn(create(key));
    }
}
