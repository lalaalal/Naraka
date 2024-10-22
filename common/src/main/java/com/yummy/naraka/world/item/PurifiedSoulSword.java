package com.yummy.naraka.world.item;

import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class PurifiedSoulSword extends SwordItem {
    public PurifiedSoulSword(Tier tier, Properties properties) {
        super(tier, properties);
    }

    @Override
    public InteractionResult useOn(UseOnContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        BlockState state = level.getBlockState(pos);
        if (state.isFaceSturdy(level, pos, Direction.UP)) {
            level.setBlock(pos.above(), NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            return InteractionResult.SUCCESS;
        }
        return super.useOn(context);
    }
}
