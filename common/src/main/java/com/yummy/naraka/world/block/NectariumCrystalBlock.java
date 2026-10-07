package com.yummy.naraka.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SpeleothemBlock;
import net.minecraft.world.level.block.state.BlockState;

public class NectariumCrystalBlock extends SpeleothemBlock {
    public NectariumCrystalBlock(BlockState blockToGrowOn, Properties properties) {
        super(blockToGrowOn, properties);
    }

    @Override
    protected int getStalactiteLandingSound() {
        return 1052;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction tipDirection = state.getValue(TIP_DIRECTION);
        BlockPos basePos = pos.relative(tipDirection.getOpposite());
        BlockState baseState = level.getBlockState(basePos);
        return baseState.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get()) || baseState.isFaceSturdy(level, basePos, tipDirection);
    }
}
