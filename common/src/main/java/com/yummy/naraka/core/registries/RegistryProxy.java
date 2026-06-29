package com.yummy.naraka.core.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.stream.Stream;

public interface RegistryProxy<T> {
    Codec<T> codec();

    Optional<? extends Holder<T>> getHolder(ResourceKey<T> key);

    Stream<T> values();

    class Simple<T> implements RegistryProxy<T> {
        private final Registry<T> registry;

        public Simple(Registry<T> registry) {
            this.registry = registry;
        }

        @Override
        public Codec<T> codec() {
            return registry.byNameCodec();
        }

        @Override
        public Optional<? extends Holder<T>> getHolder(ResourceKey<T> key) {
            return registry.getHolder(key);
        }

        @Override
        public Stream<T> values() {
            return Stream.empty();
        }
    }
}
