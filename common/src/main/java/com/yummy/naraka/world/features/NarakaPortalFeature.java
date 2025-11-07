package com.yummy.naraka.world.features;

import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.NoneFeatureConfiguration;

public class NarakaPortalFeature extends Feature<NoneFeatureConfiguration> {
    public static final BlockPos BASE_POSITION = new BlockPos(0, 10, 0);

    public NarakaPortalFeature() {
        super(NoneFeatureConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<NoneFeatureConfiguration> context) {
        BlockPos basePosition = context.origin();
        BlockState portalState = NarakaBlocks.NARAKA_PORTAL.get().defaultBlockState();
        BlockState soulLanternState = NarakaBlocks.PURIFIED_SOUL_LANTERN.get().defaultBlockState();
        WorldGenLevel level = context.level();
        level.setBlock(basePosition, soulLanternState, Block.UPDATE_ALL);
        level.setBlock(basePosition.above(), soulLanternState, Block.UPDATE_ALL);
        level.setBlock(basePosition.above(2), portalState, Block.UPDATE_ALL);
        return true;
    }
}
