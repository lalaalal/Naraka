package com.yummy.naraka.data.worldgen.placement;

import com.yummy.naraka.data.worldgen.features.NarakaCaveFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

public class NarakaCavePlacements {
    public static final ResourceKey<PlacedFeature> DIAMOND_ORE_PILLAR = NarakaPlacements.create("diamond_ore_pillar");
    public static final ResourceKey<PlacedFeature> DEEPSLATE_DIAMOND_ORE_PILLAR = NarakaPlacements.create("deepslate_diamond_ore_pillar");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(
                context,
                DIAMOND_ORE_PILLAR,
                configuredFeatures.getOrThrow(NarakaCaveFeatures.DIAMOND_ORE_PILLAR),
                CountPlacement.of(18),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(15)),
                BiomeFilter.biome()
        );
        PlacementUtils.register(
                context,
                DEEPSLATE_DIAMOND_ORE_PILLAR,
                configuredFeatures.getOrThrow(NarakaCaveFeatures.DEEPSLATE_DIAMOND_ORE_PILLAR),
                CountPlacement.of(12),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.bottom(), VerticalAnchor.absolute(0)),
                BiomeFilter.biome()
        );
    }
}
