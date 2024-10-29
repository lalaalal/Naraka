package com.yummy.naraka.neoforge.data;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.placement.NarakaOrePlacements;
import com.yummy.naraka.tags.ConventionalTags;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.levelgen.GenerationStep;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.neoforged.neoforge.common.world.BiomeModifier;
import net.neoforged.neoforge.common.world.BiomeModifiers;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

public class NarakaBiomeModifiers {
    public static final ResourceKey<BiomeModifier> NECTARIUM_GENERATION = create("nectarium_generation");

    public static void bootstrap(BootstrapContext<BiomeModifier> context) {
        HolderGetter<Biome> biomes = context.lookup(Registries.BIOME);
        HolderGetter<PlacedFeature> features = context.lookup(Registries.PLACED_FEATURE);

        HolderSet<Biome> overworldBiomes = biomes.getOrThrow(ConventionalTags.Biomes.IS_OVERWORLD);

        context.register(NECTARIUM_GENERATION,
                new BiomeModifiers.AddFeaturesBiomeModifier(
                        overworldBiomes,
                        HolderSet.direct(features::getOrThrow,
                                NarakaOrePlacements.NECTARIUM_ORE_BURIED_PLACED_KEY,
                                NarakaOrePlacements.NECTARIUM_ORE_SMALL_PLACED_KEY,
                                NarakaOrePlacements.NECTARIUM_ORE_LARGE_PLACED_KEY
                        ),
                        GenerationStep.Decoration.UNDERGROUND_ORES
                )
        );
    }

    private static ResourceKey<BiomeModifier> create(String name) {
        return ResourceKey.create(NeoForgeRegistries.Keys.BIOME_MODIFIERS, NarakaMod.location(name));
    }
}
