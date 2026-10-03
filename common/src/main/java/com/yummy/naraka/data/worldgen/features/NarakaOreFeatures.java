package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.BlockReplacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.OreFeature;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;

import java.util.List;

public class NarakaOreFeatures {
    public static final ResourceKey<Feature> NECTARIUM_ORE_SMALL = NarakaFeatures.create("nectarium_ore_small");
    public static final ResourceKey<Feature> NECTARIUM_ORE_LARGE = NarakaFeatures.create("nectarium_ore_large");
    public static final ResourceKey<Feature> NECTARIUM_ORE_BURIED = NarakaFeatures.create("nectarium_ore_buried");

    public static final ResourceKey<Feature> AMETHYST_ORE = NarakaFeatures.create("amethyst_ore");

    protected static void bootstrap(BootstrapContext<Feature> context) {
        RuleTest stone = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslate = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);
        List<BlockReplacement> nectariumTargetStates = List.of(
                BlockReplacement.replace(stone, NarakaBlocks.NECTARIUM_ORE.get().defaultBlockState()),
                BlockReplacement.replace(deepslate, NarakaBlocks.DEEPSLATE_NECTARIUM_ORE.get().defaultBlockState())
        );

        context.register(NECTARIUM_ORE_SMALL, new OreFeature(nectariumTargetStates, 4, 0.5f));
        context.register(NECTARIUM_ORE_LARGE, new OreFeature(nectariumTargetStates, 12, 0.7f));
        context.register(NECTARIUM_ORE_BURIED, new OreFeature(nectariumTargetStates, 8, 1));

        List<BlockReplacement> amethystTargetStates = List.of(
                BlockReplacement.replace(stone, NarakaBlocks.AMETHYST_ORE.get().defaultBlockState()),
                BlockReplacement.replace(deepslate, NarakaBlocks.DEEPSLATE_AMETHYST_ORE.get().defaultBlockState())
        );
        context.register(AMETHYST_ORE, new OreFeature(amethystTargetStates, 6, 0.6f));
    }
}
