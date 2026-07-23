package com.yummy.naraka.core.registries;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import org.jspecify.annotations.Nullable;

import java.util.function.Supplier;

/**
 * Proxy to delegate registration for fabric, neoforge
 *
 * @param <T> Registry value type
 * @see RegistryProxy#register(ResourceKey, String, Supplier)
 * @see HolderProxy
 */
public interface RegistryProxy<T> {
    /**
     * Register value for given registry key
     *
     * @param registryKey Registry key
     * @param name        Name of value
     * @param value       Supplier of value
     * @param <T>         Registry value type
     * @param <V>         Derived registry value type
     * @return Holder for given value
     * @see HolderProxy
     */
    static <T, V extends T> HolderProxy<T, V> register(ResourceKey<Registry<T>> registryKey, String name, Supplier<V> value) {
        return RegistryProxyProvider.get(registryKey)
                .register(name, value);
    }

    /**
     * Register value for given registry key
     *
     * @param registryKey Registry key
     * @param id          Key of value
     * @param value       Supplier of value
     * @param <T>         Registry value type
     * @param <V>         Derived registry value type
     * @return Holder for given value
     * @see HolderProxy
     */
    static <T, V extends T> HolderProxy<T, V> register(ResourceKey<Registry<T>> registryKey, Identifier id, Supplier<V> value) {
        return RegistryProxyProvider.get(registryKey)
                .register(id, value);
    }

    /**
     * Register value for given registry key
     *
     * @param key   Key of value
     * @param value Supplier of value
     * @param <T>   Registry value type
     * @param <V>   Derived registry value type
     * @return Holder for given value
     * @see HolderProxy
     */
    static <T, V extends T> HolderProxy<T, V> register(ResourceKey<T> key, Supplier<V> value) {
        return RegistryProxyProvider.get(key.registryKey())
                .register(key.identifier(), value);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    default Registry<T> getRegistry() {
        return (Registry<T>) BuiltInRegistries.REGISTRY.getValue(getRegistryKey().identifier());
    }

    default Registry<T> getRegistryOrThrow() {
        Registry<T> registry = getRegistry();
        if (registry == null)
            throw new IllegalStateException("No registry found for " + getRegistryKey().identifier());
        return registry;
    }

    ResourceKey<? extends Registry<T>> getRegistryKey();

    <V extends T> HolderProxy<T, V> register(String name, Supplier<V> value);

    <V extends T> HolderProxy<T, V> register(Identifier id, Supplier<V> value);

    default <V extends T> HolderProxy<T, V> createHolder(Identifier id) {
        return new HolderProxy<>(getRegistryOrThrow(), id);
    }

    default void onRegistrationFinished() {

    }
}
