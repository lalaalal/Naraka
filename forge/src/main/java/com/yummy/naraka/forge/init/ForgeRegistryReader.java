package com.yummy.naraka.forge.init;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.registries.RegistryReader;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.MappedRegistry;
import net.minecraft.resources.ResourceKey;
import net.minecraftforge.registries.IForgeRegistry;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.stream.Stream;

public class ForgeRegistryReader<T> implements RegistryReader<T> {
    @Nullable
    private IForgeRegistry<T> registry;
    @Nullable
    private HolderOwner<T> owner;

    public static <T> ForgeRegistryReader<T> of(IForgeRegistry<T> registry) {
        ForgeRegistryReader<T> registryReader = new ForgeRegistryReader<>();
        registryReader.registry = registry;
        return registryReader;
    }

    @SuppressWarnings("unchecked")
    public void setRegistry(IForgeRegistry<T> registry) {
        this.registry = registry;
        MappedRegistry<T> wrapper = (MappedRegistry<T>) registry.getSlaveMap(ForgeRegistryFactory.WRAPPER_ID, MappedRegistry.class);
        if (wrapper != null)
            this.owner = wrapper.holderOwner();
    }

    @Override
    public Codec<T> codec() {
        if (registry == null)
            throw new IllegalStateException("Registry is null");
        return registry.getCodec();
    }

    @Override
    public Optional<HolderOwner<T>> owner() {
        return Optional.ofNullable(owner);
    }

    @Override
    public Optional<Holder.Reference<T>> getHolder(ResourceKey<T> key) {
        if (registry == null)
            return Optional.empty();
        return registry.getDelegate(key);
    }

    @Override
    public Stream<T> values() {
        if (registry == null)
            return Stream.empty();
        return registry.getValues().stream();
    }
}
