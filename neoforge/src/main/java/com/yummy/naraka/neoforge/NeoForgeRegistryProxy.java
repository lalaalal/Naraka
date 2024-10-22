package com.yummy.naraka.neoforge;

import com.mojang.datafixers.util.Either;
import com.yummy.naraka.core.registries.NarakaRegisterProxy;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.neoforged.neoforge.registries.RegisterEvent;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

public abstract class NeoForgeRegistryProxy<T> implements NarakaRegisterProxy<T> {
    protected final LinkedHashMap<ResourceKey<T>, T> map = new LinkedHashMap<>();

    @Override
    public Holder<T> registerForHolder(String name, T value) {
        map.put(createKey(name), value);
        return holder(createKey(name), value);
    }

    public void register(RegisterEvent.RegisterHelper<T> registry) {
        for (Map.Entry<ResourceKey<T>, T> entry : map.entrySet())
            registry.register(entry.getKey(), entry.getValue());
    }

    public Holder<T> holder(ResourceKey<T> key, T value) {
        return new SimpleHolder(key, value);
    }

    protected abstract ResourceKey<T> createKey(String name);

    private class SimpleHolder implements Holder<T> {
        private final ResourceKey<T> key;
        private final T value;

        public SimpleHolder(ResourceKey<T> key, T value) {
            this.key = key;
            this.value = value;
        }

        @Override
        public T value() {
            return value;
        }

        @Override
        public boolean isBound() {
            return false;
        }

        @Override
        public boolean is(ResourceLocation location) {
            return key.location().equals(location);
        }

        @Override
        public boolean is(ResourceKey<T> resourceKey) {
            return key.equals(resourceKey);
        }

        @Override
        public boolean is(Predicate<ResourceKey<T>> predicate) {
            return predicate.test(key);
        }

        @Override
        public boolean is(TagKey<T> tagKey) {
            return false;
        }

        @Override
        public boolean is(Holder<T> holder) {
            return false;
        }

        @Override
        public Stream<TagKey<T>> tags() {
            return Stream.empty();
        }

        @Override
        public Either<ResourceKey<T>, T> unwrap() {
            return Either.left(key);
        }

        @Override
        public Optional<ResourceKey<T>> unwrapKey() {
            return Optional.of(key);
        }

        @Override
        public Kind kind() {
            return Kind.REFERENCE;
        }

        @Override
        public boolean canSerializeIn(HolderOwner<T> owner) {
            return false;
        }
    }
}
