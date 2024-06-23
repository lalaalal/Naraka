package com.yummy.naraka.world.block;

import com.yummy.naraka.world.block.entity.NarakaSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.WallSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class NarakaWallSignBlock extends WallSignBlock {
    public NarakaWallSignBlock(WoodType woodType, Properties properties) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new NarakaSignBlockEntity(pos, blockState);
    }
}
