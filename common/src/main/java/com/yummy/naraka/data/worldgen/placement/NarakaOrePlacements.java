package com.yummy.naraka.data.worldgen.placement;

import com.yummy.naraka.data.worldgen.features.NarakaOreFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

import java.util.List;

public class NarakaOrePlacements {
    public static final ResourceKey<PlacedFeature> NECTARIUM_ORE_SMALL = NarakaPlacements.create("nectarium_ore_small");
    public static final ResourceKey<PlacedFeature> NECTARIUM_ORE_LARGE = NarakaPlacements.create("nectarium_ore_large");
    public static final ResourceKey<PlacedFeature> NECTARIUM_ORE_BURIED = NarakaPlacements.create("nectarium_ore_buried");

    public static final ResourceKey<PlacedFeature> AMETHYST_ORE = NarakaPlacements.create("amethyst_ore");


    protected static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(
                context,
                NECTARIUM_ORE_SMALL,
                configuredFeatures.getOrThrow(NarakaOreFeatures.NECTARIUM_ORE_SMALL),
                commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
        PlacementUtils.register(
                context,
                NECTARIUM_ORE_LARGE,
                configuredFeatures.getOrThrow(NarakaOreFeatures.NECTARIUM_ORE_LARGE),
                rareOrePlacement(9, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
        PlacementUtils.register(
                context,
                NECTARIUM_ORE_BURIED,
                configuredFeatures.getOrThrow(NarakaOreFeatures.NECTARIUM_ORE_BURIED),
                commonOrePlacement(4, HeightRangePlacement.triangle(VerticalAnchor.aboveBottom(-80), VerticalAnchor.aboveBottom(80)))
        );
        PlacementUtils.register(
                context,
                AMETHYST_ORE,
                configuredFeatures.getOrThrow(NarakaOreFeatures.AMETHYST_ORE),
                commonOrePlacement(7, HeightRangePlacement.triangle(VerticalAnchor.absolute(-60), VerticalAnchor.absolute(60)))
        );
    }

    private static List<PlacementModifier> orePlacement(PlacementModifier countPlacement, PlacementModifier heightRange) {
        return List.of(countPlacement, InSquarePlacement.spread(), heightRange, BiomeFilter.biome());
    }

    private static List<PlacementModifier> commonOrePlacement(int count, PlacementModifier heightRange) {
        return orePlacement(CountPlacement.of(count), heightRange);
    }

    private static List<PlacementModifier> rareOrePlacement(int chance, PlacementModifier heightRange) {
        return orePlacement(RarityFilter.onAverageOnceEvery(chance), heightRange);
    }
}
