package com.yummy.naraka.world;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.NarakaConfiguredWorldCarvers;
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
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

import java.util.List;

public class NarakaBiomes {
    public static final ResourceKey<Biome> YUMMY = create("yummy");

    public static Biome yummy(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        MobSpawnSettings.Builder mobSettings = new MobSpawnSettings.Builder();
        BiomeGenerationSettings.Builder biomeGenerationSettings = new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers);
        biomeGenerationSettings.addCarver(GenerationStep.Carving.AIR, NarakaConfiguredWorldCarvers.YUMMY);
        BiomeDefaultFeatures.addDefaultCrystalFormations(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultMonsterRoom(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultUndergroundVariety(biomeGenerationSettings);
        BiomeDefaultFeatures.addSurfaceFreezing(biomeGenerationSettings);
        BiomeDefaultFeatures.addPlainGrass(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultOres(biomeGenerationSettings);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.AMETHYST_ORE);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.SINGLE_ORE_PILLAR);
        biomeGenerationSettings.addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaOrePlacements.MIXED_ORE_PILLAR);
        BiomeDefaultFeatures.addDefaultSoftDisks(biomeGenerationSettings);
        BiomeDefaultFeatures.addPlainVegetation(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultMushrooms(biomeGenerationSettings);
        BiomeDefaultFeatures.addDefaultExtraVegetation(biomeGenerationSettings);
        Music music = Musics.createGameMusic(SoundEvents.MUSIC_BIOME_DEEP_DARK);
        return OverworldBiomesInvoker.invokeBiome(true, 0.8F, 0.4F, mobSettings, biomeGenerationSettings, music);
    }

    public static void initialize(NarakaInitializer initializer) {
        initializer.registerFeatureBiomeModifier(
                "nectarium",
                ConventionalTags.Biomes.IS_OVERWORLD,
                GenerationStep.Decoration.UNDERGROUND_ORES,
                List.of(
                        NarakaOrePlacements.NECTARIUM_ORE_BURIED,
                        NarakaOrePlacements.NECTARIUM_ORE_SMALL,
                        NarakaOrePlacements.NECTARIUM_ORE_LARGE
                )
        );
    }

    private static ResourceKey<Biome> create(String name) {
        return ResourceKey.create(Registries.BIOME, NarakaMod.location(name));
    }
}
