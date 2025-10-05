package com.yummy.naraka.world;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.placement.NarakaCavePlacements;
import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.tags.ConventionalTags;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;
import java.util.function.Supplier;

public class NarakaBiomes {
    public static final ResourceKey<Biome> HEROBRINE = create("herobrine");

    public static void initialize(NarakaInitializer initializer) {
        Modifier modifier = initializer.getBiomeModifier();
        modifier.addFeatures(
                "add_nectarium_ore_generation",
                ConventionalTags.Biomes.IS_OVERWORLD,
                GenerationStep.Decoration.UNDERGROUND_ORES,
                List.of(
                        NarakaOrePlacements.NECTARIUM_ORE_BURIED,
                        NarakaOrePlacements.NECTARIUM_ORE_SMALL,
                        NarakaOrePlacements.NECTARIUM_ORE_LARGE
                )
        );
        modifier.addFeatures(
                "add_amethyst_ore_generation",
                ConventionalTags.Biomes.IS_OVERWORLD,
                GenerationStep.Decoration.UNDERGROUND_ORES,
                List.of(NarakaOrePlacements.AMETHYST_ORE)
        );
        modifier.addFeatures(
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
        return ResourceKey.create(Registries.BIOME, NarakaMod.location(name));
    }

    public interface Modifier {
        void addFeatures(String name, TagKey<Biome> biomes, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features);

        <T extends Mob> void addSpawns(String name, TagKey<Biome> biomes, MobCategory spawnGroup, Supplier<EntityType<T>> entityType, int weight, int minGroupSize, int maxGroupSize);
    }
}
