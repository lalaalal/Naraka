package com.yummy.naraka.fabric.data;

import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class SimpleDataProvider<K, V> implements DataProvider {
    protected final PackOutput.PathProvider pathProvider;
    protected final Codec<V> codec;

    protected SimpleDataProvider(FabricDataOutput output, PackOutput.Target target, String kind, Codec<V> codec) {
        this.pathProvider = output.createPathProvider(target, kind);
        this.codec = codec;
    }

    protected abstract void register(BiConsumer<ResourceKey<K>, V> output);

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<ResourceKey<K>, V> map = new HashMap<>();
        this.register(map::putIfAbsent);
        return DataProvider.saveAll(output, codec, this.pathProvider::json, map);
    }

}
