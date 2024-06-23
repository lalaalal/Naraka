package com.yummy.naraka.world.block;

import com.yummy.naraka.world.block.entity.NarakaHangingSignBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.CeilingHangingSignBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.WoodType;

public class NarakaCeilingHangingSignBlock extends CeilingHangingSignBlock {
    public NarakaCeilingHangingSignBlock(WoodType woodType, Properties properties) {
        super(woodType, properties);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState blockState) {
        return new NarakaHangingSignBlockEntity(pos, blockState);
    }
}
