package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class NarakaBiomeData {
    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        context.register(NarakaBiomes.HEROBRINE, herobrine(placedFeatures));
    }

    private static Biome herobrine(HolderGetter<PlacedFeature> placedFeatures) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0xffffff)
                                .waterFogColor(0xffffff)
                                .grassColorOverride(0xffffff)
                                .foliageColorOverride(0xffffff)
                                .backgroundMusic(NarakaMusics.HEROBRINE_PHASE_4)
                                .fogColor(0xffffff)
                                .skyColor(0xffffff)
                                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                                .build()
                )
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(new BiomeGenerationSettings.PlainBuilder()
                        .addFeature(GenerationStep.Decoration.RAW_GENERATION, placedFeatures.getOrThrow(NarakaPlacements.NARAKA_PORTAL))
                        .build()
                )
                .build();
    }
}
