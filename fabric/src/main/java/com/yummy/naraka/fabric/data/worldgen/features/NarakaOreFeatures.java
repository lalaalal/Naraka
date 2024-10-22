package com.yummy.naraka.fabric.data.worldgen.features;

import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class NarakaOreFeatures {
    protected static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> nectariumTargetStates = List.of(
                OreConfiguration.target(stone, NarakaBlocks.NECTARIUM_ORE.defaultBlockState()),
                OreConfiguration.target(deepslate, NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.defaultBlockState())
        );

        FeatureUtils.register(context, com.yummy.naraka.world.features.NarakaOreFeatures.NECTARIUM_ORE_SMALL, Feature.ORE, new OreConfiguration(nectariumTargetStates, 4, 0.5f));
        FeatureUtils.register(context, com.yummy.naraka.world.features.NarakaOreFeatures.NECTARIUM_ORE_LARGE, Feature.ORE, new OreConfiguration(nectariumTargetStates, 12, 0.7f));
        FeatureUtils.register(context, com.yummy.naraka.world.features.NarakaOreFeatures.NECTARIUM_ORE_BURIED, Feature.ORE, new OreConfiguration(nectariumTargetStates, 8, 1));
    }
}
