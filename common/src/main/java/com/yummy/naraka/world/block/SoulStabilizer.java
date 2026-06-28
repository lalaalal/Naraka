package com.yummy.naraka.world.block;

import com.yummy.naraka.core.particles.SoulParticleOption;
import com.yummy.naraka.util.NarakaItemUtils;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.List;

public class SoulStabilizer extends BaseEntityBlock {
    public SoulStabilizer(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return box(5.5f, 0, 5.5f, 10.5f, 5, 10.5f);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulStabilizerBlockEntity(pos, state);
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (blockEntity instanceof SoulStabilizerBlockEntity soulStabilizerBlockEntity
                && soulStabilizerBlockEntity.canInject(stack)) {
            int consumeCount = soulStabilizerBlockEntity.tryInject(player, stack, !player.onGround());
            stack.shrink(consumeCount);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(SoulParticleOption.with(soulStabilizerBlockEntity.getSoulType()),
                        pos.getX() + 0.5, pos.getY() + 0.4, pos.getZ() + 0.5,
                        30,
                        0.1, 0.1, 0.1,
                        1
                );
            }
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.CONSUME;
    }

    @SuppressWarnings("deprecation")
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder params) {
        ServerLevel level = params.getLevel();
        BlockEntity blockEntity = params.getOptionalParameter(LootContextParams.BLOCK_ENTITY);
        if (blockEntity instanceof SoulStabilizerBlockEntity soulStabilizerBlockEntity) {
            return super.getDrops(state, params)
                    .stream().peek(
                            itemStack -> NarakaItemUtils.saveBlockEntity(itemStack, soulStabilizerBlockEntity)
                    ).toList();
        }
        return super.getDrops(state, params);
    }
}
