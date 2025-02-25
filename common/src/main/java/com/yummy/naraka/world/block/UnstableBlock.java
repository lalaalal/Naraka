package com.yummy.naraka.world.block;

import com.yummy.naraka.tags.NarakaEntityTypeTags;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

public class UnstableBlock extends Block {
    public UnstableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.getType().is(NarakaEntityTypeTags.HEROBRINE)) {
            level.destroyBlock(pos, false);
            level.setBlock(pos.below(), Blocks.LAVA.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            entity.igniteForSeconds(2);
        }
    }
}
