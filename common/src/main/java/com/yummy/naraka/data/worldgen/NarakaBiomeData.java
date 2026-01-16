package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.attribute.AmbientSounds;
import net.minecraft.world.attribute.BackgroundMusic;
import net.minecraft.world.attribute.EnvironmentAttributes;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.BiomeGenerationSettings;
import net.minecraft.world.level.biome.BiomeSpecialEffects;
import net.minecraft.world.level.biome.MobSpawnSettings;

public class NarakaBiomeData {
    public static void bootstrap(BootstrapContext<Biome> context) {
        context.register(NarakaBiomes.HEROBRINE, herobrine());
    }

    private static Biome herobrine() {
        return new Biome.BiomeBuilder()
                .hasPrecipitation(false)
                .temperature(0.5F)
                .downfall(0)
                .specialEffects(
                        new BiomeSpecialEffects.Builder()
                                .waterColor(0xffffff)
                                .grassColorOverride(0xffffff)
                                .foliageColorOverride(0xffffff)
                                .build()
                )
                .setAttribute(EnvironmentAttributes.FOG_COLOR, 0xffffff)
                .setAttribute(EnvironmentAttributes.SKY_COLOR, 0xffffff)
                .setAttribute(EnvironmentAttributes.WATER_FOG_COLOR, 0xffffff)
                .setAttribute(EnvironmentAttributes.BACKGROUND_MUSIC, new BackgroundMusic(NarakaMusics.HEROBRINE_PHASE_4))
                .setAttribute(EnvironmentAttributes.AMBIENT_SOUNDS, AmbientSounds.EMPTY)
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }
}
