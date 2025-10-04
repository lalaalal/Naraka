package com.yummy.naraka.world.block;

import com.yummy.naraka.world.block.entity.ForgingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
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

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof ForgingBlockEntity forgingBlockEntity) {
            if (itemStack.is(Items.MACE)) {
                if (forgingBlockEntity.tryReinforce())
                    itemStack.hurtAndBreak(5, player, EquipmentSlot.MAINHAND);
                return InteractionResult.SUCCESS;
            } else if (!forgingBlockEntity.getForgingItem().isEmpty()) {
                forgingBlockEntity.dropForgingItem();
                return InteractionResult.SUCCESS;
            } else if (forgingBlockEntity.canReinforce(itemStack)) {
                forgingBlockEntity.setForgingItem(itemStack);
                itemStack.consume(1, player);
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
