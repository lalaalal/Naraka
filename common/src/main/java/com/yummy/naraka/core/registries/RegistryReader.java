package com.yummy.naraka.core.registries;

import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

import java.util.Optional;
import java.util.stream.Stream;

public interface RegistryReader<T> {
    ResourceKey<? extends Registry<T>> registryKey();

    Codec<T> codec();

    Optional<HolderOwner<T>> owner();

    Optional<Holder.Reference<T>> getHolder(ResourceKey<T> key);

    default Optional<Holder.Reference<T>> getHolder(ResourceLocation id) {
        return getHolder(ResourceKey.create(registryKey(), id));
    }

    Optional<Holder<T>> wrapHolder(T value);

    Stream<T> values();

    class Simple<T> implements RegistryReader<T> {
        private final Registry<T> registry;

        public Simple(Registry<T> registry) {
            this.registry = registry;
        }

        @Override
        public ResourceKey<? extends Registry<T>> registryKey() {
            return registry.key();
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
        public Optional<Holder<T>> wrapHolder(T value) {
            return Optional.of(registry.wrapAsHolder(value));
        }

        @Override
        public Stream<T> values() {
            return registry.stream();
        }
    }
}
