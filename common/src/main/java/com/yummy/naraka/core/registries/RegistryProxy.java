package com.yummy.naraka.core.registries;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

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
     * @param key   Registry key
     * @param name  Name of value
     * @param value Supplier of value
     * @param <T>   Registry value type
     * @param <V>   Derived registry value type
     * @return Holder for given value
     * @see HolderProxy
     */
    static <T, V extends T> HolderProxy<T, V> register(ResourceKey<Registry<T>> key, String name, Supplier<V> value) {
        return RegistryProxyProvider.get(key)
                .register(name, value);
    }

    @SuppressWarnings("unchecked")
    @Nullable
    default Registry<T> getRegistry() {
        return (Registry<T>) BuiltInRegistries.REGISTRY.get(getRegistryKey().location());
    }

    default Registry<T> getRegistryOrThrow() {
        Registry<T> registry = getRegistry();
        if (registry == null)
            throw new IllegalStateException("No registry found for " + getRegistryKey().location());
        return registry;
    }

    ResourceKey<? extends Registry<T>> getRegistryKey();

    <V extends T> HolderProxy<T, V> register(String name, Supplier<V> value);

    default <V extends T> HolderProxy<T, V> createHolder(String name) {
        return new HolderProxy<>(getRegistryOrThrow(), NarakaMod.location(name));
    }

    default void onRegistrationFinished() {

    }
}
