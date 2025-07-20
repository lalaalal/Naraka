package com.yummy.naraka.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TransparentBlock extends Block {
    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");
    public static final BooleanProperty COLLISION = BooleanProperty.create("collision");

    public TransparentBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                this.stateDefinition.any()
                        .setValue(VISIBLE, false)
                        .setValue(COLLISION, true)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VISIBLE).add(COLLISION);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(VISIBLE))
            return super.getShape(state, level, pos, context);
        if (context instanceof EntityCollisionContext entityCollisionContext
                && entityCollisionContext.getEntity() instanceof Player player) {
            if (player.isCreative())
                return super.getShape(state, level, pos, context);
        }
        return Shapes.empty();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        if (state.getValue(COLLISION))
            return Shapes.block();
        return Shapes.empty();
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return defaultBlockState().setValue(VISIBLE, true).setValue(COLLISION, true);
    }

    @Override
    protected InteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level level, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHitResult) {
        if (player.isCreative() && itemStack.is(NarakaBlocks.TRANSPARENT_BLOCK.get().asItem())) {
            if (player.onGround()) {
                level.setBlock(pos, blockState.cycle(VISIBLE), 10);
            } else {
                level.setBlock(pos, blockState.cycle(VISIBLE).cycle(COLLISION), 10);
            }

            return InteractionResult.SUCCESS;
        }
        return super.useItemOn(itemStack, blockState, level, pos, player, pHand, pHitResult);
    }

    @Override
    protected void spawnDestroyParticles(Level level, Player player, BlockPos pos, BlockState state) {
        if (state.getValue(VISIBLE))
            super.spawnDestroyParticles(level, player, pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        if (state.getValue(VISIBLE))
            return super.getRenderShape(state);
        return RenderShape.INVISIBLE;
    }
}