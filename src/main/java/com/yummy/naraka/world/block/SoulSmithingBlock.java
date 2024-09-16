package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.world.block.entity.ForgingBlockEntity;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.entity.SoulSmithingBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.Nullable;

public class SoulSmithingBlock extends ForgingBlock {
    private static final MapCodec<SoulSmithingBlock> CODEC = simpleCodec(SoulSmithingBlock::new);

    public SoulSmithingBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SoulSmithingBlockEntity soulSmithingBlockEntity) {
            if (stack.isEmpty()
                    && hitResult.getDirection() == Direction.WEST
                    && soulSmithingBlockEntity.isStabilizerAttached()) {
                soulSmithingBlockEntity.detachSoulStabilizer();
                return ItemInteractionResult.SUCCESS;
            } else if (stack.isEmpty()
                    && hitResult.getDirection() == Direction.SOUTH
                    && !soulSmithingBlockEntity.getTemplateItem().isEmpty()) {
                soulSmithingBlockEntity.detachTemplateItem();
                return ItemInteractionResult.SUCCESS;
            } else if (soulSmithingBlockEntity.tryAttachSoulStabilizer(stack)) {
                stack.consume(1, player);
                return ItemInteractionResult.SUCCESS;
            } else if (soulSmithingBlockEntity.tryAttachTemplate(stack)) {
                stack.consume(1, player);
                return ItemInteractionResult.SUCCESS;
            }
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulSmithingBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide)
            return super.getTicker(level, state, type);
        return createTickerHelper(type, NarakaBlockEntityTypes.SOUL_SMITHING, ForgingBlockEntity::serverTick);
    }
}
