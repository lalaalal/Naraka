package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.world.features.NarakaFeatures;
import com.yummy.naraka.world.features.configurations.OrePillarConfiguration;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.worldgen.features.FeatureUtils;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;

public class NarakaCaveFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> DIAMOND_ORE_PILLAR = NarakaConfiguredFeatures.create("diamond_ore_pillar");
    public static final ResourceKey<ConfiguredFeature<?, ?>> DEEPSLATE_DIAMOND_ORE_PILLAR = NarakaConfiguredFeatures.create("deepslate_diamond_ore_pillar");

    public static void bootstrap(BootstrapContext<ConfiguredFeature<?, ?>> context) {
        FeatureUtils.register(context, DIAMOND_ORE_PILLAR, NarakaFeatures.ORE_PILLAR.get(), new OrePillarConfiguration(
                block(Blocks.STONE), HolderSet.direct(block(Blocks.DIAMOND_ORE)), 32, UniformInt.of(18, 32), UniformInt.of(4, 7), 0.4f, 0.9f, true
        ));
        FeatureUtils.register(context, DEEPSLATE_DIAMOND_ORE_PILLAR, NarakaFeatures.ORE_PILLAR.get(), new OrePillarConfiguration(
                block(Blocks.DEEPSLATE), HolderSet.direct(block(Blocks.DEEPSLATE_DIAMOND_ORE)), 48, UniformInt.of(36, 48), UniformInt.of(5, 8), 0.48f, 0.9f, true
        ));
    }

    private static Holder<Block> block(Block block) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(block);
    }
}
