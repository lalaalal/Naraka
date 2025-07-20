package com.yummy.naraka.fabric.data;

import com.yummy.naraka.data.worldgen.NarakaDimensionTypes;
import com.yummy.naraka.data.worldgen.NarakaDimensions;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.dimension.DimensionType;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.FlatLevelSource;

import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

public class NarakaDimensionProvider extends DataProviderWithRegistry<LevelStem, LevelStem> {
    protected NarakaDimensionProvider(FabricDataOutput output, CompletableFuture<HolderLookup.Provider> registryLookup) {
        super(output, PackOutput.Target.DATA_PACK, "dimension", LevelStem.CODEC, registryLookup);
    }

    @Override
    protected void register(HolderLookup.Provider registryLookup, BiConsumer<ResourceKey<LevelStem>, LevelStem> output) {
        HolderLookup<DimensionType> dimensionType = registryLookup.lookupOrThrow(Registries.DIMENSION_TYPE);
        HolderLookup<Biome> biomes = registryLookup.lookupOrThrow(Registries.BIOME);
        output.accept(NarakaDimensions.NARAKA, new LevelStem(
                dimensionType.getOrThrow(NarakaDimensionTypes.NARAKA),
                new FlatLevelSource(NarakaDimensions.getNarakaSettings(biomes))
        ));
    }

    @Override
    public String getName() {
        return "Dimension";
    }
}
