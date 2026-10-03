package com.yummy.naraka.data.worldgen.features;

import com.yummy.naraka.world.features.OrePillarFeature;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.feature.Feature;

public class NarakaCaveFeatures {
    public static final ResourceKey<Feature> DIAMOND_ORE_PILLAR = NarakaFeatures.create("diamond_ore_pillar");
    public static final ResourceKey<Feature> DEEPSLATE_DIAMOND_ORE_PILLAR = NarakaFeatures.create("deepslate_diamond_ore_pillar");

    public static void bootstrap(BootstrapContext<Feature> context) {
        context.register(DIAMOND_ORE_PILLAR, new OrePillarFeature(
                block(Blocks.STONE), HolderSet.direct(block(Blocks.DIAMOND_ORE)), 32, UniformInt.of(18, 32), UniformInt.of(4, 7), 0.4f, 0.9f, true
        ));
        context.register(DEEPSLATE_DIAMOND_ORE_PILLAR, new OrePillarFeature(
                block(Blocks.DEEPSLATE), HolderSet.direct(block(Blocks.DEEPSLATE_DIAMOND_ORE)), 48, UniformInt.of(36, 48), UniformInt.of(5, 8), 0.48f, 0.9f, true
        ));
    }

    private static Holder<Block> block(Block block) {
        return BuiltInRegistries.BLOCK.wrapAsHolder(block);
    }
}
