package com.yummy.naraka.data.worldgen.placement;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.placement.PlacedFeature;

public class NarakaPlacements {
    public static void bootstrap(BootstrapContext<PlacedFeature> context) {
        NarakaOrePlacements.bootstrap(context);
    }

    public static ResourceKey<PlacedFeature> createKey(String name) {
        return ResourceKey.create(Registries.PLACED_FEATURE, NarakaMod.location(name));
    }
}
