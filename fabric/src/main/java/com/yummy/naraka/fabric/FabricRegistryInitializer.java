package com.yummy.naraka.fabric;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;

import java.util.function.Supplier;

public class FabricRegistryInitializer extends RegistryInitializer {
    @Override
    protected <T> RegistryProxy<T> create(ResourceKey<Registry<T>> key) {
        return new FabricRegistryProxy<>(key);
    }

    protected static class FabricRegistryProxy<T> implements RegistryProxy<T> {
        private final ResourceKey<Registry<T>> key;

        public FabricRegistryProxy(ResourceKey<Registry<T>> key) {
            this.key = key;
        }

        @SuppressWarnings("unchecked")
        private Registry<T> getRegistry() {
            return (Registry<T>) BuiltInRegistries.REGISTRY.get(key.location());
        }

        @Override
        public ResourceKey<Registry<T>> getRegistryKey() {
            return key;
        }

        @Override
        public <V extends T> LazyHolder<T, V> register(String name, Supplier<V> value) {
            Registry.register(getRegistry(), NarakaMod.location(name), value.get());
            return createHolder(name);
        }
    }
}
