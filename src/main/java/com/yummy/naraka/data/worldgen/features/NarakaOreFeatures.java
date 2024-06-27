package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class NarakaOreFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> NECTARIUM_ORE_SMALL = NarakaFeatures.createKey("nectarium_ore_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NECTARIUM_ORE_LARGE = NarakaFeatures.createKey("nectarium_ore_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NECTARIUM_ORE_BURIED = NarakaFeatures.createKey("nectarium_ore_buried");

    protected static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> nectariumTargetStates = List.of(
                OreConfiguration.target(stone, NarakaBlocks.NECTARIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslate, NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get().defaultBlockState())
        );

        FeatureUtils.register(context, NECTARIUM_ORE_SMALL, Feature.ORE, new OreConfiguration(nectariumTargetStates, 4, 0.5f));
        FeatureUtils.register(context, NECTARIUM_ORE_LARGE, Feature.ORE, new OreConfiguration(nectariumTargetStates, 12, 0.7f));
        FeatureUtils.register(context, NECTARIUM_ORE_BURIED, Feature.ORE, new OreConfiguration(nectariumTargetStates, 8, 1));
    }
}
