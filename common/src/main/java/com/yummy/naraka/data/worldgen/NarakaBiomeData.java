package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.*;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.carver.ConfiguredWorldCarver;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class NarakaBiomeData {
    public static void bootstrap(BootstrapContext<Biome> context) {
        HolderGetter<PlacedFeature> placedFeatures = context.lookup(Registries.PLACED_FEATURE);
        HolderGetter<ConfiguredWorldCarver<?>> worldCarvers = context.lookup(Registries.CONFIGURED_CARVER);
        context.register(NarakaBiomes.HEROBRINE, herobrine(placedFeatures, worldCarvers));
    }

    private static Biome herobrine(HolderGetter<PlacedFeature> placedFeatures, HolderGetter<ConfiguredWorldCarver<?>> worldCarvers) {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0x333333)
                                .waterFogColor(0x666666)
                                .grassColorOverride(0x666666)
                                .foliageColorOverride(0x666666)
                                .backgroundMusic(NarakaMusics.HEROBRINE_PHASE_4)
                                .fogColor(0)
                                .skyColor(0)
                                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                                .build()
                )
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaPlacements.PURIFIED_SOUL_LANTERN)
                        .build()
                )
                .build();
    }
}
