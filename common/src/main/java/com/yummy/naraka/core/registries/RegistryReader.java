package com.yummy.naraka.core.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;

import java.util.Optional;
import java.util.stream.Stream;

public interface RegistryReader<T> {
    Codec<T> codec();

    Optional<HolderOwner<T>> owner();

    Optional<Holder.Reference<T>> getHolder(ResourceKey<T> key);

    Stream<T> values();

    class Simple<T> implements RegistryReader<T> {
        private final Registry<T> registry;

        public Simple(Registry<T> registry) {
            this.registry = registry;
        }

        @Override
        public Codec<T> codec() {
            return registry.byNameCodec();
        }

        @Override
        public Optional<HolderOwner<T>> owner() {
            return Optional.of(registry.holderOwner());
        }

        @Override
        public Optional<Holder.Reference<T>> getHolder(ResourceKey<T> key) {
            return registry.getHolder(key);
        }

        @Override
        public Stream<T> values() {
            return Stream.empty();
        }
    }
}
