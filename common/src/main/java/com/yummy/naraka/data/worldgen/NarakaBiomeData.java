package com.yummy.naraka.data.worldgen;

import com.yummy.naraka.sounds.NarakaMusics;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.biome.*;

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
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }
}
