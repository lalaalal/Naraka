package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;

public class SoulStabilizer extends BaseEntityBlock {
    private static final MapCodec<SoulStabilizer> CODEC = simpleCodec(SoulStabilizer::new);

    protected SoulStabilizer(Properties properties) {
        super(properties);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulStabilizerBlockEntity(pos, state);
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SoulStabilizerBlockEntity soulStabilizerBlockEntity
                && soulStabilizerBlockEntity.canInject(stack)) {
            soulStabilizerBlockEntity.inject(stack);
            stack.consume(1, player);
            if (level instanceof ServerLevel serverLevel)
                serverLevel.sendParticles(ParticleTypes.ASH,
                        pos.getX() + 0.5, pos.getY() + 0.9, pos.getZ() + 0.5,
                        10,
                        0.2, 0.1, 0.2,
                        0.5
                );

            return ItemInteractionResult.CONSUME;
        }
        return ItemInteractionResult.sidedSuccess(level.isClientSide);
    }
}
