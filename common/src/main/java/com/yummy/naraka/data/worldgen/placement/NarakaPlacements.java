package com.yummy.naraka.data.worldgen.placement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.features.NarakaFeatures;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.placement.BiomeFilter;
import net.minecraft.world.level.levelgen.placement.HeightRangePlacement;
import net.minecraft.world.level.levelgen.placement.InSquarePlacement;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class NarakaPlacements {
    public static final ResourceKey<PlacedFeature> PURIFIED_SOUL_LANTERN = create("purified_soul_lantern");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        NarakaOrePlacements.bootstrap(context);
        NarakaCavePlacements.bootstrap(context);

        HolderGetter<Feature> features = context.lookup(Registries.FEATURE);

        PlacementUtils.register(context,
                PURIFIED_SOUL_LANTERN,
                features.getOrThrow(NarakaFeatures.PURIFIED_SOUL_LANTERN),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(56), VerticalAnchor.absolute(60)),
                BiomeFilter.biome()
        );
    }

    public static ResourceKey<PlacedFeature> create(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, NarakaMod.identifier(name));
    }
}
