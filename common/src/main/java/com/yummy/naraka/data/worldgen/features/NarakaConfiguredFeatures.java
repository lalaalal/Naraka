package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.features.NarakaFeatures;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.ReplaceBlockConfiguration;

public class NarakaConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> PURIFIED_SOUL_LANTERN = create("purified_soul_lantern");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NARAKA_PORTAL = create("naraka_portal");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        NarakaTreeFeatures.bootstrap(context);
        NarakaOreFeatures.bootstrap(context);
        NarakaCaveFeatures.bootstrap(context);

        FeatureUtils.register(context, PURIFIED_SOUL_LANTERN, Feature.REPLACE_SINGLE_BLOCK, new ReplaceBlockConfiguration(
                        NarakaBlocks.TRANSPARENT_BLOCK.get().defaultBlockState(),
                        NarakaBlocks.PURIFIED_SOUL_LANTERN.get().defaultBlockState()
                )
        );
        FeatureUtils.register(context, NARAKA_PORTAL, NarakaFeatures.NARAKA_PORTAL.get());
    }

    public static ResourceKey<ConfiguredFeature<?, ?>> create(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, NarakaMod.location(name));
    }
}
