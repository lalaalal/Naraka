package com.yummy.naraka.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.BlockHitResult;

public class NectariumCoreBlock extends Block {
    public static final BooleanProperty ACTIVATED = BooleanProperty.create("activated");

    public static final int MAX_HEIGHT = 5;

    public static int lightLevel(BlockState blockState) {
        return blockState.getValue(ACTIVATED) ? 4 : 0;
    }

    public NectariumCoreBlock(Properties properties) {
        super(properties.randomTicks());
        registerDefaultState(this.stateDefinition.any()
                .setValue(ACTIVATED, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(ACTIVATED);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (state.getValue(ACTIVATED)) {
            BlockPos growingPos = findUpFaceSturdyBlock(level, pos).above();
            if (growingPos.equals(pos) || pos.getY() - growingPos.getY() > MAX_HEIGHT)
                return;

            BlockState crystal = NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.defaultBlockState();
            level.setBlock(growingPos, crystal, UPDATE_ALL_IMMEDIATE);
        }
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.HONEY_BOTTLE)) {
            BlockState activatedState = state.setValue(ACTIVATED, true);
            level.setBlock(pos, activatedState, UPDATE_ALL_IMMEDIATE);
            if (!player.isCreative())
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            return ItemInteractionResult.CONSUME;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        super.animateTick(state, level, pos, random);
    }

    private BlockPos findUpFaceSturdyBlock(ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = pos.below().mutable();
        BlockState searchingState = level.getBlockState(mutableBlockPos);
        while (!searchingState.isFaceSturdy(level, mutableBlockPos, Direction.UP)) {
            mutableBlockPos.move(Direction.DOWN);
            searchingState = level.getBlockState(mutableBlockPos);
        }
        return mutableBlockPos.immutable();
    }
}
