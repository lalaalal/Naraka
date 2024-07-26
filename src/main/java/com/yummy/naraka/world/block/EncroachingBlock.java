package com.yummy.naraka.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class EncroachingBlock extends Block {
    private static final double COMPOSE_CHANCE = 0.5;

    private final Block targetBlock;

    public EncroachingBlock(Properties properties, Block targetBlock) {
        super(properties.randomTicks());
        this.targetBlock = targetBlock;
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction direction : UPDATE_SHAPE_ORDER) {
            mutablePos.setWithOffset(pos, direction);
            if (level.getBlockState(mutablePos.immutable()).is(targetBlock)
                    && random.nextFloat() < COMPOSE_CHANCE) {
                level.setBlock(mutablePos.immutable(), defaultBlockState(), 2);
                return;
            }
        }
    }
}
