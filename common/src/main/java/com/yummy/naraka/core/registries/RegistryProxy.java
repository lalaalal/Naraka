package com.yummy.naraka.core.registries;

import com.yummy.naraka.NarakaMod;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public interface RegistryProxy<T> {
    static <T, V extends T> LazyHolder<T, V> register(ResourceKey<Registry<T>> key, String name, Supplier<V> value) {
        return RegistryInitializer.get(key)
                .register(name, value);
    }

    ResourceKey<Registry<T>> getRegistryKey();

    <V extends T> LazyHolder<T, V> register(String name, Supplier<V> value);

    default <V extends T> LazyHolder<T, V> createHolder(String name) {
        return new LazyHolder<>(getRegistryKey(), NarakaMod.location(name));
    }

    default void onRegistrationFinished() {

    }
}
