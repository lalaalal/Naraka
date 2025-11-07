package com.yummy.naraka.data.worldgen.placement;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.data.worldgen.features.NarakaConfiguredFeatures;
import com.yummy.naraka.world.features.NarakaPortalFeature;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.placement.PlacementUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.VerticalAnchor;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.placement.*;

public class NarakaPlacements {
    public static final ResourceKey<PlacedFeature> PURIFIED_SOUL_LANTERN = create("purified_soul_lantern");
    public static final ResourceKey<PlacedFeature> NARAKA_PORTAL = create("naraka_portal");

    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        NarakaOrePlacements.bootstrap(context);
        NarakaCavePlacements.bootstrap(context);

        HolderGetter<ConfiguredFeature<?, ?>> configuredFeatures = context.lookup(Registries.CONFIGURED_FEATURE);

        PlacementUtils.register(context,
                PURIFIED_SOUL_LANTERN,
                configuredFeatures.getOrThrow(NarakaConfiguredFeatures.PURIFIED_SOUL_LANTERN),
                InSquarePlacement.spread(),
                HeightRangePlacement.uniform(VerticalAnchor.absolute(0), VerticalAnchor.absolute(8)),
                BiomeFilter.biome()
        );
        PlacementUtils.register(context,
                NARAKA_PORTAL,
                configuredFeatures.getOrThrow(NarakaConfiguredFeatures.NARAKA_PORTAL),
                FixedPlacement.of(NarakaPortalFeature.BASE_POSITION),
                BiomeFilter.biome()
        );
    }

    public static ResourceKey<PlacedFeature> create(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, NarakaMod.location(name));
    }
}
