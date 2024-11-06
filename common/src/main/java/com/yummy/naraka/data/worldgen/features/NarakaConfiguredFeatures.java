package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class NarakaConfiguredFeatures {
    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        NarakaTreeFeatures.bootstrap(context);
        NarakaOreFeatures.bootstrap(context);
        NarakaCaveFeatures.bootstrap(context);
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, NarakaMod.location(name));
    }
}
