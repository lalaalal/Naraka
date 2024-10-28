package com.yummy.naraka.world;

import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.tags.ConventionalTags;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.List;

public class NarakaBiomes {
    public static void initialize(NarakaInitializer initializer) {
        initializer.registerFeatureBiomeModifier(
                "nectarium",
                ConventionalTags.Biomes.IS_OVERWORLD,
                GenerationStep.Decoration.UNDERGROUND_ORES,
                List.of(
                        NarakaOrePlacements.NECTARIUM_ORE_BURIED_PLACED_KEY,
                        NarakaOrePlacements.NECTARIUM_ORE_SMALL_PLACED_KEY,
                        NarakaOrePlacements.NECTARIUM_ORE_LARGE_PLACED_KEY
                )
        );
    }
}
