package com.yummy.naraka.fabric.data;

import com.yummy.naraka.data.worldgen.NarakaDimensionTypes;
import com.yummy.naraka.data.worldgen.NarakaLevelStems;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricCodecDataProvider;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class NarakaLevelStemProvider extends FabricCodecDataProvider<LevelStem> {
    protected NarakaLevelStemProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, registryLookup, Registries.LEVEL_STEM, LevelStem.CODEC);
    }

    @Override
    protected void configure(BiConsumer<ResourceLocation, LevelStem> provider, HolderLookup.Provider lookup) {
        HolderLookup<DimensionType> dimensionType = lookup.lookupOrThrow(Registries.DIMENSION_TYPE);
        HolderLookup<Biome> biomes = lookup.lookupOrThrow(Registries.BIOME);
        provider.accept(NarakaLevelStems.NARAKA.location(), new LevelStem(
                dimensionType.getOrThrow(NarakaDimensionTypes.NARAKA),
                new FlatLevelSource(NarakaLevelStems.getNarakaSettings(biomes))
        ));
    }

    @Override
    public String getName() {
        return "Dimension";
    }
}
