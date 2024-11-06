package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.tags.ConventionalTags;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.features.NarakaFeatures;
import com.yummy.naraka.world.features.configurations.OrePillarConfiguration;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class NarakaOreFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> NECTARIUM_ORE_SMALL = NarakaConfiguredFeatures.create("nectarium_ore_small");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NECTARIUM_ORE_LARGE = NarakaConfiguredFeatures.create("nectarium_ore_large");
    public static final ResourceKey<ConfiguredFeature<?, ?>> NECTARIUM_ORE_BURIED = NarakaConfiguredFeatures.create("nectarium_ore_buried");

    public static final ResourceKey<ConfiguredFeature<?, ?>> AMETHYST_ORE = NarakaConfiguredFeatures.create("amethyst_ore");
    public static final ResourceKey<ConfiguredFeature<?, ?>> SINGLE_ORE_PILLAR = NarakaConfiguredFeatures.create("single_ore_pillar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> MIXED_ORE_PILLAR = NarakaConfiguredFeatures.create("mixed_ore_pillar");

    protected static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        HolderGetter<Block> blocks = context.lookup(Registries.BLOCK);
        HolderSet<Block> ores = blocks.getOrThrow(ConventionalTags.Blocks.ORES);

        RuleTest stone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<OreConfiguration.TargetBlockState> nectariumTargetStates = List.of(
                OreConfiguration.target(stone, NarakaBlocks.NECTARIUM_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslate, NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get().defaultBlockState())
        );

        FeatureUtils.register(context, NECTARIUM_ORE_SMALL, Feature.ORE, new OreConfiguration(nectariumTargetStates, 4, 0.5f));
        FeatureUtils.register(context, NECTARIUM_ORE_LARGE, Feature.ORE, new OreConfiguration(nectariumTargetStates, 12, 0.7f));
        FeatureUtils.register(context, NECTARIUM_ORE_BURIED, Feature.ORE, new OreConfiguration(nectariumTargetStates, 8, 1));

        List<OreConfiguration.TargetBlockState> amethystTargetStates = List.of(
                OreConfiguration.target(stone, NarakaBlocks.AMETHYST_ORE.get().defaultBlockState()),
                OreConfiguration.target(deepslate, NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get().defaultBlockState())
        );
        FeatureUtils.register(context, AMETHYST_ORE, Feature.ORE, new OreConfiguration(amethystTargetStates, 16, 0));

        FeatureUtils.register(context, SINGLE_ORE_PILLAR, NarakaFeatures.ORE_PILLAR.get(), new OrePillarConfiguration(
                ores, 16, UniformInt.of(6, 8), UniformInt.of(3, 5), 0.9f, true
        ));
        FeatureUtils.register(context, MIXED_ORE_PILLAR, NarakaFeatures.ORE_PILLAR.get(), new OrePillarConfiguration(
                ores, 32, UniformInt.of(18, 32), UniformInt.of(4, 7), 0.9f, false
        ));
    }
}
