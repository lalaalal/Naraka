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

/**
 * A {@link Holder.Reference} supplying derived value for forge registration
 *
 * @param <T> Registry value type
 * @param <V> Derived value type
 */
public class HolderProxy<T, V extends T> implements Holder<T>, Supplier<V> {
    @Nullable
    private Holder<T> holder;
    private final ResourceKey<T> key;
    private final Supplier<V> valueSupplier;
    @Nullable
    private HolderOwner<T> owner;

    public HolderProxy(Registry<T> registry, ResourceLocation name, Supplier<V> valueSupplier) {
        this.key = ResourceKey.create(registry.key(), name);
        this.owner = registry.holderOwner();
        this.valueSupplier = valueSupplier;
    }

    public HolderProxy(ResourceKey<T> key, Supplier<V> valueSupplier) {
        this.key = key;
        this.valueSupplier = valueSupplier;
    }

    protected void bind(boolean throwOnMissing) {
        if (holder != null)
            return;
        Optional<Registry<T>> optionalRegistry = findRegistry(throwOnMissing);
        if (optionalRegistry.isEmpty())
            return;
        optionalRegistry.ifPresent(registry -> owner = registry.holderOwner());
        Optional<Reference<T>> found = findReference(optionalRegistry.get(), throwOnMissing);
        found.ifPresent(reference -> {
            this.holder = reference;
        });
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
        if (holder == null)
            bind(false);
        if (holder == null)
            return valueSupplier.get();
        return (V) holder.value();
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
    public boolean is(TagKey<T> tagKey) {
        bind(false);
        return holder != null && holder.is(tagKey);
    }

    @Override
    public boolean is(Predicate<ResourceKey<T>> predicate) {
        bind(false);
        return holder != null && holder.is(predicate);
    }

    @Override
    public boolean is(ResourceKey<T> resourceKey) {
        bind(false);
        return holder != null && holder.is(resourceKey);
    }

    @Override
    public boolean is(ResourceLocation location) {
        bind(false);
        return holder != null && holder.is(location);
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
        if (this.owner == null)
            return false;
        return this.owner.canSerializeIn(owner);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        bind(false);
        if (holder != null) return holder.equals(obj);
        return obj instanceof Holder<?> h
                && h.kind() == this.kind()
                && h.unwrapKey().orElse(null) == key;
    }

    @Override
    public int hashCode() {
        bind(false);
        if (holder != null)
            return holder.hashCode();
        return key.hashCode();
    }
}
