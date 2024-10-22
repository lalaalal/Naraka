package com.yummy.naraka.world.block;

import com.yummy.naraka.world.block.grower.NarakaTreeGrowers;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.SaplingBlock;
import net.minecraft.world.level.block.grower.TreeGrower;
import net.minecraft.world.level.block.state.BlockState;

public class EbonySaplingBlock extends SaplingBlock {
    public EbonySaplingBlock(TreeGrower treeGrower, Properties properties) {
        super(treeGrower, properties);
    }

    public EbonySaplingBlock(Properties properties) {
        super(NarakaTreeGrowers.EBONY, properties);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return level.getBlockState(pos.below()).is(NarakaBlocks.COMPRESSED_IRON_BLOCK);
    }
}
