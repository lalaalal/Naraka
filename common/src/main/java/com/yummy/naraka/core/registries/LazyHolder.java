package com.yummy.naraka.core.registries;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.Stream;

public class LazyHolder<T, V extends T> implements Holder<T>, Supplier<V> {
    private final ResourceKey<T> key;
    private @Nullable Holder<T> holder;

    public LazyHolder(ResourceKey<Registry<T>> registryKey, ResourceLocation name) {
        this.key = ResourceKey.create(registryKey, name);
    }

    public LazyHolder(ResourceKey<T> key) {
        this.key = key;
    }

    protected void bind(boolean throwOnMissing) {
        if (holder != null)
            return;
        Optional<Registry<T>> registry = findRegistry(throwOnMissing);
        if (registry.isEmpty())
            return;
        Optional<Reference<T>> found = findReference(registry.get(), throwOnMissing);
        found.ifPresent(reference -> this.holder = reference);
    }

    @SuppressWarnings("unchecked")
    private Optional<Registry<T>> findRegistry(boolean throwOnMissing) {
        Optional<Registry<T>> registry = (Optional<Registry<T>>) BuiltInRegistries.REGISTRY.getOptional(key.registry());
        if (registry.isEmpty() && throwOnMissing)
            throw new IllegalStateException(key.registry() + " does not exist");
        return registry;
    }

    private Optional<Reference<T>> findReference(Registry<T> registry, boolean throwOnMissing) {
        Optional<Reference<T>> found = registry.getHolder(key);
        if (found.isEmpty() && throwOnMissing)
            throw new IllegalStateException(key + " is not registered");
        return found;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V get() {
        return (V) value();
    }

    @Override
    public T value() {
        if (holder == null)
            bind(true);
        return holder.value();
    }

    @Override
    public boolean isBound() {
        bind(false);
        return holder != null && holder.isBound();
    }

    @Override
    public boolean is(ResourceLocation location) {
        return location.equals(key.location());
    }

    @Override
    public boolean is(ResourceKey<T> resourceKey) {
        return key == resourceKey;
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        return predicate.test(key);
    }

    @Override
    public boolean is(TagKey<T> tagKey) {
        bind(false);
        return holder != null && holder.is(tagKey);
    }

    @SuppressWarnings("deprecation")
    @Override
    public boolean is(Holder<T> holder) {
        bind(false);
        return holder != null && holder.is(holder);
    }

    @Override
    public Stream<TagKey<T>> tags() {
        bind(false);
        return holder == null ? Stream.empty() : holder.tags();
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
        bind(false);
        return holder != null && holder.canSerializeIn(owner);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        bind(false);
        if (holder != null) return holder.equals(obj);
        return obj instanceof Holder<?> h
                && h.kind() == this.kind()
                && h.unwrapKey().orElse(null) == this.key;
    }

    @Override
    public int hashCode() {
        bind(false);
        if (holder != null)
            return holder.hashCode();
        return key.hashCode();
    }
}
