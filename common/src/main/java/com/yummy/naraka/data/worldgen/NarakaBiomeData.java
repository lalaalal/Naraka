package com.yummy.naraka.data.worldgen;

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
                                .waterColor(0xababab)
                                .waterFogColor(0xbcbcbc)
                                .fogColor(0x888888)
                                .skyColor(0)
                                .ambientMoodSound(AmbientMoodSettings.LEGACY_CAVE_SETTINGS)
                                .build()
                )
                .mobSpawnSettings(MobSpawnSettings.EMPTY)
                .generationSettings(BiomeGenerationSettings.EMPTY)
                .build();
    }
}
