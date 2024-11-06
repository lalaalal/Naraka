package com.yummy.naraka.core.registries;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.RegistryInitializer;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import org.jetbrains.annotations.Nullable;

import java.util.function.Supplier;

public interface RegistryProxy<T> {
    static <T, V extends T> LazyHolder<T, V> register(ResourceKey<Registry<T>> key, String name, Supplier<V> value) {
        return RegistryInitializer.get(key)
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

    ResourceKey<Registry<T>> getRegistryKey();

    <V extends T> LazyHolder<T, V> register(String name, Supplier<V> value);

    default <V extends T> LazyHolder<T, V> createHolder(String name) {
        return new LazyHolder<>(getRegistryOrThrow(), NarakaMod.location(name));
    }

    default void onRegistrationFinished() {

    }
}
