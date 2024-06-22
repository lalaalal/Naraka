package com.yummy.naraka.world.block;

import com.yummy.naraka.NarakaUtil;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class TransparentBlock extends Block {
    public static final BooleanProperty VISIBLE = BooleanProperty.create("visible");

    public TransparentBlock(BlockBehaviour.Properties properties) {
        super(properties);
        registerDefaultState(
                this.stateDefinition
                        .any()
                        .setValue(VISIBLE, true)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(VISIBLE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        Player player = NarakaUtil.getClientPlayer();
        if (state.getValue(VISIBLE) || (player != null && player.isCreative()))
            return super.getShape(state, pLevel, pPos, pContext);
        return Shapes.empty();
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack itemStack, BlockState blockState, Level pLevel, BlockPos pos, Player player, InteractionHand pHand, BlockHitResult pHitResult) {
        if (player.isCreative() && itemStack.is(NarakaBlocks.TRANSPARENT_BLOCK.asItem())) {
            pLevel.setBlock(pos, blockState.cycle(VISIBLE), 10);
            return ItemInteractionResult.sidedSuccess(pLevel.isClientSide);
        }
        return super.useItemOn(itemStack, blockState, pLevel, pos, player, pHand, pHitResult);
    }

    @SuppressWarnings("deprecation")
    @Override
    protected RenderShape getRenderShape(BlockState state) {
        if (state.getValue(VISIBLE))
            return super.getRenderShape(state);
        return RenderShape.INVISIBLE;
    }
}