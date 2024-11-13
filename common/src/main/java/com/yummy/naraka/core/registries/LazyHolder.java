package com.yummy.naraka.core.registries;

import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.function.Supplier;
import java.util.stream.Stream;

/**
 * A {@link Holder.Reference} supplying derived value for neoforge registration
 *
 * @param <T> Registry value type
 * @param <V> Derived value type
 */
public class LazyHolder<T, V extends T> extends Holder.Reference<T> implements Supplier<V> {
    private @Nullable Holder<T> holder;

    public LazyHolder(Registry<T> registry, ResourceLocation name) {
        super(Type.STAND_ALONE, registry.holderOwner(), ResourceKey.create(registry.key(), name), null);
    }

    protected void bind(boolean throwOnMissing) {
        if (holder != null)
            return;
        Optional<Registry<T>> registry = findRegistry(throwOnMissing);
        if (registry.isEmpty())
            return;
        Optional<Reference<T>> found = findReference(registry.get(), throwOnMissing);
        found.ifPresent(reference -> {
            this.holder = reference;
            bindValue(holder.value());
        });
    }

    @SuppressWarnings("unchecked")
    private Optional<Registry<T>> findRegistry(boolean throwOnMissing) {
        Optional<Registry<T>> registry = (Optional<Registry<T>>) BuiltInRegistries.REGISTRY.getOptional(key().registry());
        if (registry.isEmpty() && throwOnMissing)
            throw new IllegalStateException(key().registry() + " does not exist");
        return registry;
    }

    private Optional<Reference<T>> findReference(Registry<T> registry, boolean throwOnMissing) {
        Optional<Reference<T>> found = registry.getHolder(key());
        if (found.isEmpty() && throwOnMissing)
            throw new IllegalStateException(key() + " is not registered");
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
    public boolean equals(Object obj) {
        if (this == obj) return true;
        bind(false);
        if (holder != null) return holder.equals(obj);
        return obj instanceof Holder<?> h
                && h.kind() == this.kind()
                && h.unwrapKey().orElse(null) == key();
    }

    @Override
    public int hashCode() {
        bind(false);
        if (holder != null)
            return holder.hashCode();
        return key().hashCode();
    }
}
