package com.yummy.naraka.world;

import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import net.fabricmc.fabric.api.biome.v1.BiomeModifications;
import net.fabricmc.fabric.api.biome.v1.BiomeSelectors;
import net.minecraft.world.level.levelgen.GenerationStep;

public class NarakaBiomes {
    public static void initialize() {
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_BURIED_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_SMALL_PLACED_KEY);
        BiomeModifications.addFeature(BiomeSelectors.foundInOverworld(), GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_LARGE_PLACED_KEY);
    }
}
