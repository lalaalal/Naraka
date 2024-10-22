package com.yummy.naraka.fabric.data.worldgen.features;

import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class NarakaFeatures {
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        NarakaTreeFeatures.bootstrap(context);
        NarakaOreFeatures.bootstrap(context);
    }
}
