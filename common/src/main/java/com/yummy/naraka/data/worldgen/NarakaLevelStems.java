package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.NarakaBiomes;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.dimension.LevelStem;
import net.minecraft.world.level.levelgen.flat.FlatLayerInfo;
import net.minecraft.world.level.levelgen.flat.FlatLevelGeneratorSettings;

import java.util.List;
import java.util.Optional;

public class NarakaLevelStems {
    public static final ResourceKey<LevelStem> NARAKA = create("naraka");

    public static FlatLevelGeneratorSettings getNarakaSettings(HolderGetter<Biome> biomes) {
        FlatLevelGeneratorSettings flatLevelGeneratorSettings = new FlatLevelGeneratorSettings(
                Optional.empty(), biomes.getOrThrow(NarakaBiomes.HEROBRINE), List.of()
        );
        flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(63, NarakaBlocks.TRANSPARENT_BLOCK.get()));
        flatLevelGeneratorSettings.getLayersInfo().add(new FlatLayerInfo(1, Blocks.WATER));
        flatLevelGeneratorSettings.setDecoration();
        flatLevelGeneratorSettings.updateLayers();
        return flatLevelGeneratorSettings;
    }

    private static ResourceKey<LevelStem> create(String name) {
        return ResourceKey.create(Registries.LEVEL_STEM, NarakaMod.location(name));
    }
}
