package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.features.NarakaFeatures;
import com.yummy.naraka.world.rootplacer.EbonyRootPlacer;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.valueproviders.ConstantInt;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.util.valueproviders.WeightedListInt;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.featuresize.TwoLayersFeatureSize;
import net.minecraft.world.level.levelgen.feature.foliageplacers.CherryFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.foliageplacers.FancyFoliagePlacer;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;
import net.minecraft.world.level.levelgen.feature.trunkplacers.CherryTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;

import java.util.Optional;
import java.util.OptionalInt;

public class NarakaTreeFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> EBONY = NarakaFeatures.create("ebony");
    public static final ResourceKey<ConfiguredFeature<?, ?>> EBONY_CHERRY = NarakaFeatures.create("ebony_cherry");

    protected static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, EBONY, Feature.TREE, createEbony().build());
        FeatureUtils.register(context, EBONY_CHERRY, Feature.TREE, createCherryEbony().build());
    }

    private static TreeConfiguration.TreeConfigurationBuilder createEbony() {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(NarakaBlocks.EBONY_LOG.get()),
                new FancyTrunkPlacer(4, 8, 8),
                BlockStateProvider.simple(NarakaBlocks.EBONY_LEAVES.get()),
                new FancyFoliagePlacer(ConstantInt.of(2), ConstantInt.of(4), 4),
                new TwoLayersFeatureSize(1, 0, 1, OptionalInt.of(4))
        ).ignoreVines().dirt(BlockStateProvider.simple(NarakaBlocks.EBONY_ROOTS.get()));
    }

    private static TreeConfiguration.TreeConfigurationBuilder createCherryEbony() {
        return new TreeConfiguration.TreeConfigurationBuilder(
                BlockStateProvider.simple(NarakaBlocks.EBONY_LOG.get().branchBlockState()),
                new CherryTrunkPlacer(
                        7, 1, 0,
                        new WeightedListInt(SimpleWeightedRandomList.<IntProvider>builder()
                                .add(ConstantInt.of(1), 1)
                                .add(ConstantInt.of(2), 1)
                                .add(ConstantInt.of(3), 1)
                                .build()
                        ),
                        UniformInt.of(2, 4),
                        UniformInt.of(-4, -3),
                        UniformInt.of(-1, 0)
                ),
                BlockStateProvider.simple(NarakaBlocks.EBONY_LEAVES.get()),
                new CherryFoliagePlacer(ConstantInt.of(4), ConstantInt.of(0), ConstantInt.of(5), 0.25F, 0.5F, 0.16666667F, 0.33333334F),
                Optional.of(
                        new EbonyRootPlacer(
                                UniformInt.of(0, 0),
                                BlockStateProvider.simple(NarakaBlocks.EBONY_ROOTS.get()),
                                Optional.empty(),
                                UniformInt.of(3, 4),
                                UniformInt.of(4, 7),
                                0.45f
                        )
                ),
                new TwoLayersFeatureSize(1, 0, 2)
        ).ignoreVines().dirt(BlockStateProvider.simple(NarakaBlocks.EBONY_ROOTS.get()));
    }
}
