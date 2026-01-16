package com.yummy.naraka.world;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.placement.NarakaCavePlacements;
import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.init.BiomeModificationRegistry;
import com.yummy.naraka.tags.ConventionalTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;

import java.util.List;

public class NarakaBiomes {
    public static final ResourceKey<Biome> HEROBRINE = create("herobrine");

    public static void initialize() {
        BiomeModificationRegistry.addFeatures(
                "add_nectarium_ore_generation",
                ConventionalTags.Biomes.IS_OVERWORLD,
                GenerationStep.Decoration.UNDERGROUND_ORES,
                List.of(
                        NarakaOrePlacements.NECTARIUM_ORE_BURIED,
                        NarakaOrePlacements.NECTARIUM_ORE_SMALL,
                        NarakaOrePlacements.NECTARIUM_ORE_LARGE
                )
        );
        BiomeModificationRegistry.addFeatures(
                "add_amethyst_ore_generation",
                ConventionalTags.Biomes.IS_OVERWORLD,
                GenerationStep.Decoration.UNDERGROUND_ORES,
                List.of(NarakaOrePlacements.AMETHYST_ORE)
        );
        BiomeModificationRegistry.addFeatures(
                "add_diamond_pillars",
                ConventionalTags.Biomes.IS_OVERWORLD,
                GenerationStep.Decoration.UNDERGROUND_DECORATION,
                List.of(
                        NarakaCavePlacements.DIAMOND_ORE_PILLAR,
                        NarakaCavePlacements.DEEPSLATE_DIAMOND_ORE_PILLAR
                )
        );
    }

    private static ResourceKey<Biome> create(String name) {
        return ResourceKey.create(Registries.BIOME, NarakaMod.identifier(name));
    }
}
