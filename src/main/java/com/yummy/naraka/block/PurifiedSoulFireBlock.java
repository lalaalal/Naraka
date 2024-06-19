package com.yummy.naraka.block;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseFireBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoulFireBlock;
import net.minecraft.world.level.block.state.BlockState;

public class PurifiedSoulFireBlock extends BaseFireBlock {
    public static final MapCodec<PurifiedSoulFireBlock> CODEC = simpleCodec(PurifiedSoulFireBlock::new);

    public PurifiedSoulFireBlock(Properties properties) {
        super(properties, 5f);
    }

    @Override
    protected MapCodec<? extends BaseFireBlock> codec() {
        return CODEC;
    }

    @Override
    protected boolean canBurn(BlockState state) {
        return false;
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockState belowBlock = level.getBlockState(pos.below());
        return SoulFireBlock.canSurviveOnBlock(belowBlock);
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction pDirection, BlockState pNeighborState, LevelAccessor level, BlockPos pos, BlockPos pNeighborPos) {
        if (canSurvive(state, level, pos))
            return state;
        return Blocks.AIR.defaultBlockState();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return defaultBlockState();
    }
}
