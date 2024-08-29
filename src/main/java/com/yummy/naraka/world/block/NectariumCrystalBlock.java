package com.yummy.naraka.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.PointedDripstoneBlock;
import net.minecraft.world.level.block.state.BlockState;

public class NectariumCrystalBlock extends PointedDripstoneBlock {
    public NectariumCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (!level.getBlockTicks().hasScheduledTick(pos, this))
            level.scheduleTick(pos, this, 1);

        return state;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurvive(state, level, pos))
            level.destroyBlock(pos, true);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {

    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {

    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowBlockState = level.getBlockState(pos.below());
        return belowBlockState.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK) || belowBlockState.isFaceSturdy(level, pos.below(), Direction.UP);
    }
}
