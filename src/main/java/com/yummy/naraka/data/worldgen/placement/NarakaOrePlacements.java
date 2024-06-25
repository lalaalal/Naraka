package com.yummy.naraka.data.worldgen.placement;

import java.util.List;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.features.NarakaFeatures;
import com.yummy.naraka.data.worldgen.features.NarakaOreFeatures;

import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.CountPlacement;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;
import net.minecraft.world.level.levelgen.placement.PlacementModifier;
import net.minecraft.world.level.levelgen.placement.RarityFilter;

public class NarakaOrePlacements {
    public static final ResourceKey<PlacedFeature> NECTARIUM_ORE_SMALL_PLACED_KEY = NarakaPlacements.createKey("nectarium_ore_small_placed");
    public static final ResourceKey<PlacedFeature> NECTARIUM_ORE_LARGE_PLACED_KEY = NarakaPlacements.createKey("nectarium_ore_large_placed");
    public static final ResourceKey<PlacedFeature> NECTARIUM_ORE_BURIED_PLACED_KEY = NarakaPlacements.createKey("nectarium_ore_buried_placed");

    protected static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(
            context, 
            NECTARIUM_ORE_SMALL_PLACED_KEY, 
            configuredFeatures.getOrThrow(NarakaOreFeatures.NECTARIUM_ORE_SMALL), 
            commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
        PlacementUtils.register(
            context,
            NECTARIUM_ORE_LARGE_PLACED_KEY,
            configuredFeatures.getOrThrow(NarakaOreFeatures.NECTARIUM_ORE_LARGE), 
            rareOrePlacement(9, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
        PlacementUtils.register(
            context, 
            NECTARIUM_ORE_BURIED_PLACED_KEY, 
            configuredFeatures.getOrThrow(NarakaOreFeatures.NECTARIUM_ORE_BURIED), 
            commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
    }

        private static List<PlacementModifier> orePlacement(PlacementModifier pCountPlacement, PlacementModifier pHeightRange) {
        return List.of(pCountPlacement, InSquarePlacement.spread(), pHeightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int pCount, PlacementModifier pHeightRange) {
        return orePlacement(CountPlacement.of(pCount), pHeightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int pChance, PlacementModifier pHeightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(pChance), pHeightRange);
    }
}
