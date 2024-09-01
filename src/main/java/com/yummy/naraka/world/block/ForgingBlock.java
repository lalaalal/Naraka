package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.data.lang.NarakaLanguageProviders;
import com.yummy.naraka.world.block.entity.ForgingBlockEntity;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.item.reinforcement.Reinforcement;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class ForgingBlock extends BaseEntityBlock {
    public static final MapCodec<ForgingBlock> CODEC = simpleCodec(ForgingBlock::new);

    public ForgingBlock(Properties properties) {
        super(properties);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new ForgingBlockEntity(blockPos, blockState);
    }

    @Override
    protected VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
        return box(0, 0, 0, 16, 2, 16);
    }

    @Override
    protected RenderShape getRenderShape(BlockState blockState) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos blockPos, Player player, InteractionHand interactionHand, BlockHitResult blockHitResult) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof ForgingBlockEntity forgingBlockEntity) {
            if (itemStack.is(Items.MACE)) {
                if (forgingBlockEntity.tryReinforce()) {
                    itemStack.hurtAndBreak(5, player, EquipmentSlot.MAINHAND);
                    return ItemInteractionResult.SUCCESS;
                }
                return ItemInteractionResult.FAIL;
            } else if (!forgingBlockEntity.getItemStack().isEmpty()) {
                forgingBlockEntity.dropItem();
                return ItemInteractionResult.SUCCESS;
            } else if (Reinforcement.canReinforce(itemStack)) {
                forgingBlockEntity.setItemStack(itemStack);
                itemStack.shrink(1);
                return ItemInteractionResult.CONSUME;
            } else {
                return ItemInteractionResult.FAIL;
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    protected void onRemove(BlockState blockState, Level level, BlockPos blockPos, BlockState blockState2, boolean bl) {
        BlockEntity blockEntity = level.getBlockEntity(blockPos);
        if (blockEntity instanceof ForgingBlockEntity forgingBlockEntity)
            forgingBlockEntity.dropItem();
        super.onRemove(blockState, level, blockPos, blockState2, bl);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        tooltipComponents.add(
                Component.translatable(NarakaLanguageProviders.tooltipKey(this))
                        .withStyle(ChatFormatting.GRAY)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide)
            return super.getTicker(level, state, type);
        return createTickerHelper(type, NarakaBlockEntityTypes.FORGING_BLOCK_ENTITY, ForgingBlockEntity::serverTick);
    }
}
