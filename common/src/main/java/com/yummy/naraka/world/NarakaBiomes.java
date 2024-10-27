package com.yummy.naraka.world;

import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.tags.ConventionalTags;
import dev.architectury.registry.level.biome.BiomeModifications;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.function.Predicate;

public class NarakaBiomes {
    private static final Predicate<BiomeModifications.BiomeContext> IS_OVERWORLD = context -> {
        return context.hasTag(ConventionalTags.Biomes.IS_OVERWORLD);
    };

    public static void initialize() {
        BiomeModifications.addProperties((context, mutable) -> {
            mutable.getGenerationProperties()
                    .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_BURIED_PLACED_KEY)
                    .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_SMALL_PLACED_KEY)
                    .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.NECTARIUM_ORE_LARGE_PLACED_KEY);
        });
    }
}
