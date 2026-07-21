package com.yummy.naraka.core.registries;

import com.mojang.datafixers.util.Either;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;

import javax.annotation.Nullable;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * A {@link Holder.Reference} supplying derived value for forge registration
 *
 * @param <T> Registry value type
 * @param <V> Derived value type
 */
public class HolderProxy<T, V extends T> implements Holder<T>, ValueGetter<V> {
    @Nullable
    private Holder<T> holder;
    private final ResourceKey<T> key;
    @Nullable
    private HolderOwner<T> owner;

    public HolderProxy(Registry<T> registry, ResourceLocation name) {
        this.key = ResourceKey.create(registry.key(), name);
        this.owner = registry.holderOwner();
    }

    public HolderProxy(ResourceKey<T> key) {
        this.key = key;
    }

    protected void bind(boolean throwOnMissing) {
        if (holder != null)
            return;
        Optional<Registry<T>> optionalRegistry = findRegistry();
        optionalRegistry.ifPresentOrElse(registry -> bindFromRegistry(registry, throwOnMissing), () -> {
            findRegistryReader(throwOnMissing).ifPresent(registryReader -> {
                bindFromRegistryReader(registryReader, throwOnMissing);
            });
        });
    }

    private void bindFromRegistry(Registry<T> registry, boolean throwOnMissing) {
        owner = registry.holderOwner();
        Optional<Reference<T>> found = findReference(registry, throwOnMissing);
        found.ifPresent(reference -> {
            this.holder = reference;
        });
    }

    private void bindFromRegistryReader(RegistryReader<T> registry, boolean throwOnMissing) {
        registry.owner().ifPresent(owner -> this.owner = owner);
        Optional<Reference<T>> found = registry.getHolder(key);
        if (found.isEmpty() && throwOnMissing)
            throw new IllegalStateException("No registry found for key " + key);
        found.ifPresent(reference -> {
            this.holder = reference;
        });
    }

    @SuppressWarnings("unchecked")
    private Optional<Registry<T>> findRegistry() {
        return (Optional<Registry<T>>) BuiltInRegistries.REGISTRY.getOptional(key.registry());
    }

    private Optional<RegistryReader<T>> findRegistryReader(boolean throwOnMissing) {
        Optional<RegistryReader<T>> reader = RegistryProxyProvider.getInstance().getRegistryReader(ResourceKey.createRegistryKey(key.registry()));
        if (reader.isEmpty() && throwOnMissing)
            throw new IllegalStateException("No registry found for key " + key.registry());
        return reader;
    }

    private Optional<Reference<T>> findReference(Registry<T> registry, boolean throwOnMissing) {
        Optional<Reference<T>> found = registry.getHolder(key);
        if (found.isEmpty() && throwOnMissing)
            throw new IllegalStateException(key + " is not registered");
        return found;
    }

    @SuppressWarnings("unchecked")
    @Override
    public V getConcreteValue() {
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
