package com.yummy.naraka.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NectariumCrystalBlock extends Block {
    public NectariumCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (canSurvive(state, level, pos))
            return state;
        level.destroyBlock(pos, true);
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowBlockState = level.getBlockState(pos.below());
        return belowBlockState.isFaceSturdy(level, pos.below(), Direction.UP);
    }
}
