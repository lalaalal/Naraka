package com.yummy.naraka.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class NectariumBlock extends Block {
    private static double composeChance;

    static {
        composeChance = 0.5;
    }

    public NectariumBlock(Properties properties) {
        super(properties.randomTicks());
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        BlockPos.MutableBlockPos mutablePos = new BlockPos.MutableBlockPos();
        for (Direction direction : UPDATE_SHAPE_ORDER) {
            mutablePos.setWithOffset(pos, direction);
            if (level.getBlockState(mutablePos.immutable()).is(Blocks.HONEY_BLOCK)
                    && random.nextFloat() < composeChance) {
                level.setBlock(mutablePos.immutable(), defaultBlockState(), 2);
                return;
            }
        }
    }
}
