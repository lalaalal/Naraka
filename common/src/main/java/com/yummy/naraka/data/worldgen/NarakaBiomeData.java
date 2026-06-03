package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.data.worldgen.placement.NarakaPlacements;
import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;
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
                                .grassColorOverride(0x666666)
                                .foliageColorOverride(0x666666)
                                .build()
                )
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(NarakaMusics.HEROBRINE_PHASE_4))
                .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.EMPTY)
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(new BiomeGenerationSettings.Builder(placedFeatures, worldCarvers)
                        .addFeature(GenerationStep.Decoration.UNDERGROUND_ORES, NarakaPlacements.PURIFIED_SOUL_LANTERN)
                        .build()
                )
                .build();
    }
}
