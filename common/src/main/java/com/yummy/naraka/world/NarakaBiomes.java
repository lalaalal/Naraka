package com.yummy.naraka.world;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.placement.NarakaCavePlacements;
import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.mixin.invoker.OverworldBiomesInvoker;
import com.yummy.naraka.tags.ConventionalTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BiomeDefaultFeatures;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.Music;
import net.minecraft.sounds.Musics;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class NarakaBiomes {
    public static final ResourceKey<Biome> PILLAR_CAVE = create("pillar_cave");

    public static Biome pillarCaves(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobSettings = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeGenerationSettings = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        BiomeDefaultFeatures.addDefaultCarversAndLakes(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultCrystalFormations(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultMonsterRoom(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenerationSettings);
        BiomeDefaultFeatures.addSurfaceFreezing(biomeGenerationSettings);
        BiomeDefaultFeatures.addPlainGrass(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultOres(biomeGenerationSettings);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.AMETHYST_ORE);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NarakaCavePlacements.DIAMOND_ORE_PILLAR);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_DECORATION, NarakaCavePlacements.DEEPSLATE_DIAMOND_ORE_PILLAR);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenerationSettings);
        BiomeDefaultFeatures.addPlainVegetation(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultMushrooms(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenerationSettings);
        Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DRIPSTONE_CAVES);
        return OverworldBiomesInvoker.invokeBiome(true, 0.8F, 0.4F, mobSettings, biomeGenerationSettings, music);
    }

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
        void addFeatures(String name, TagKey<Biome> target, GenerationStep.Decoration step, List<ResourceKey<PlacedFeature>> features);
    }
}
