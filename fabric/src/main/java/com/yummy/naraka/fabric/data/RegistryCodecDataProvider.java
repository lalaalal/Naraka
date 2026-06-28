package com.yummy.naraka.fabric.data;

import com.google.gson.JsonElement;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.CachedOutput;
import net.minecraft.data.DataProvider;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public abstract class RegistryCodecDataProvider<T> implements DataProvider {
    private static final Logger LOGGER = LogUtils.getLogger();

    protected final PackOutput.PathProvider pathProvider;
    private final Codec<T> codec;
    private final CompletableFuture<HolderLookup.Provider> registries;

    public RegistryCodecDataProvider(FabricDataOutput output, PackOutput.Target target, String kind, Codec<T> codec, CompletableFuture<HolderLookup.Provider> registries) {
        this.pathProvider = output.createPathProvider(target, kind);
        this.codec = codec;
        this.registries = registries;
    }

    protected abstract void configure(BiConsumer<ResourceLocation, T> output, HolderLookup.Provider provider);

    @Override
    public CompletableFuture<?> run(CachedOutput output) {
        Map<ResourceLocation, T> map = new HashMap<>();
        return registries.thenApply(provider -> {
            configure(map::putIfAbsent, provider);
            return CompletableFuture.allOf(
                    map.entrySet().stream()
                            .map(entry -> save(output, entry.getKey(), entry.getValue()))
                            .toArray(CompletableFuture[]::new)
            );
        });
    }

    private CompletableFuture<?> save(CachedOutput output, ResourceLocation location, T value) {
        DataResult<JsonElement> result = codec.encodeStart(JsonOps.INSTANCE, value);
        JsonElement json = result.getOrThrow(false, LOGGER::error);
        Path path = pathProvider.json(location);
        return DataProvider.saveStable(output, json, path);
    }
}
