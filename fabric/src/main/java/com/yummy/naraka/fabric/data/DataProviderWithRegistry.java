package com.yummy.naraka.fabric.data;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class DataProviderWithRegistry<K, V> extends SimpleDataProvider<K, V> {
    protected final CompletableFuture<HolderLookup.Provider> registryLookup;

    protected DataProviderWithRegistry(FabricDataOutput output, PackOutput.Target target, String kind, Codec<V> codec, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, target, kind, codec);
        this.registryLookup = registryLookup;
    }

    @Override
    protected final void register(BiConsumer<ResourceKey<K>, V> output) {
    }

    protected abstract void register(HolderLookup.Provider registryLookup, BiConsumer<ResourceKey<K>, V> output);

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        return registryLookup.thenCompose(lookup -> {
            Map<ResourceKey<K>, V> map = new HashMap<>();
            this.register(lookup, map::putIfAbsent);
            return DataProvider.saveAll(output, object -> codec.encodeStart(lookup.createSerializationContext(JsonOps.INSTANCE), object).getOrThrow(), this.pathProvider::json, map);
        });
    }
}
