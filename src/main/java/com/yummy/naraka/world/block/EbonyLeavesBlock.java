package com.yummy.naraka.world.block;

import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.ParticleUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.block.state.BlockState;

public class EbonyLeavesBlock extends LeavesBlock {
    public EbonyLeavesBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource randomSource) {
        super.animateTick(blockState, level, blockPos, randomSource);
        if (randomSource.nextInt(10) == 0) {
            BlockPos blockPos2 = blockPos.below();
            BlockState blockState2 = level.getBlockState(blockPos2);
            if (!isFaceFull(blockState2.getCollisionShape(level, blockPos2), Direction.UP)) {
                ParticleUtils.spawnParticleBelow(level, blockPos, randomSource, NarakaParticleTypes.EBONY_LEAVES);
            }
        }
    }
}
