package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;

import java.util.OptionalInt;

public class NarakaTreeFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> EBONY = NarakaFeatures.createKey("ebony");

    protected static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, EBONY, Feature.TREE, createEbony().build());
    }

    private static TreeConfiguration.TreeConfigurationBuilder createEbony() {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(NarakaBlocks.EBONY_LOG),
                new FancyTrunkPlacer(8, 11, 0),
                BlockStateProvider.simple(NarakaBlocks.EBONY_LEAVES),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                new TwoLayersFeatureSize(0, 0, 0, OptionalInt.of(4))
        ).ignoreVines().dirt(BlockStateProvider.simple(Blocks.ROOTED_DIRT));
    }
}
