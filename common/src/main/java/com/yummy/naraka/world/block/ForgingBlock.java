package com.yummy.naraka.world.block;

import com.yummy.naraka.world.block.entity.ForgingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public abstract class ForgingBlock extends BaseEntityBlock {
    protected ForgingBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack itemStack = player.getItemInHand(hand);
        if (blockEntity instanceof ForgingBlockEntity forgingBlockEntity) {
            if (itemStack.is(Items.MACE)) {
                if (forgingBlockEntity.tryReinforce())
                    itemStack.hurtAndBreak(5, player, entity -> entity.broadcastBreakEvent(hand));
                return InteractionResult.SUCCESS;
            } else if (!forgingBlockEntity.getForgingItem().isEmpty()) {
                forgingBlockEntity.dropForgingItem();
                return InteractionResult.SUCCESS;
            } else if (forgingBlockEntity.canReinforce(itemStack)) {
                forgingBlockEntity.setForgingItem(itemStack);
                itemStack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.CONSUME;
    }

    @Override
    public void destroy(LevelAccessor level, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof ForgingBlockEntity forgingBlockEntity)
            forgingBlockEntity.dropItems();
        super.destroy(level, pos, state);
    }
}
