package com.yummy.naraka.world.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public class EbonyLogBlock extends RotatedPillarBlock {
    public static final BooleanProperty BRANCH = BooleanProperty.create("branch");

    public EbonyLogBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.defaultBlockState()
                .setValue(AXIS, Direction.Axis.Y)
                .setValue(BRANCH, false)
        );
    }

    public BlockState branchBlockState() {
        return defaultBlockState().setValue(BRANCH, true);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(BRANCH);
    }
}
