package com.yummy.naraka.world.block;

import com.yummy.naraka.advancements.NarakaCriteriaTriggers;
import com.yummy.naraka.advancements.criterion.SimpleTrigger;
import com.yummy.naraka.core.particles.NarakaParticleTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.BlockHitResult;

public class NectariumCoreBlock extends Block {
    public static final int MAX_HEIGHT = 10;
    public static final int MAX_HONEY = 5;
    public static final IntegerProperty HONEY = IntegerProperty.create("honey", 0, MAX_HONEY);

    public static int lightLevel(BlockState blockState) {
        return blockState.getValue(HONEY) * 2;
    }

    public NectariumCoreBlock(Properties properties) {
        super(properties.randomTicks());
        registerDefaultState(this.stateDefinition.any()
                .setValue(HONEY, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(HONEY);
    }

    @Override
    protected void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        int honey = state.getValue(HONEY);
        if (honey < 1)
            return;
        BlockState belowState = level.getBlockState(pos.below());
        if (belowState.isAir()) {
            BlockPos growingPos = pos.below();
            BlockState crystal = NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get().defaultBlockState()
                    .setValue(NectariumCrystalBlock.TIP_DIRECTION, Direction.DOWN)
                    .setValue(NectariumCrystalBlock.THICKNESS, DripstoneThickness.TIP);
            level.setBlock(growingPos, crystal, UPDATE_ALL);
            level.setBlock(pos, state.setValue(HONEY, honey - 1), UPDATE_ALL);
        } else if (belowState.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get())) {
            BlockPos growingPos = findUpFaceSturdyBlock(level, pos).above();
            if (growingPos.equals(pos) || growingPos.equals(pos.below()) || pos.getY() - growingPos.getY() > MAX_HEIGHT)
                return;
            BlockState crystal = NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get().defaultBlockState()
                    .setValue(NectariumCrystalBlock.TIP_DIRECTION, Direction.UP)
                    .setValue(NectariumCrystalBlock.THICKNESS, DripstoneThickness.TIP);
            level.setBlock(growingPos, crystal, UPDATE_ALL);
            level.setBlock(pos, state.setValue(HONEY, honey - 1), UPDATE_ALL);
        }
    }


    @Override
    protected InteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (stack.is(Items.HONEY_BOTTLE)) {
            BlockState activatedState = state.setValue(HONEY, MAX_HONEY);
            level.setBlock(pos, activatedState, UPDATE_ALL_IMMEDIATE);
            if (!player.isCreative())
                player.setItemInHand(hand, new ItemStack(Items.GLASS_BOTTLE));
            if (player instanceof ServerPlayer serverPlayer)
                NarakaCriteriaTriggers.SIMPLE_TRIGGER.get().trigger(serverPlayer, SimpleTrigger.ACTIVATE_NECTARIUM_CORE);
            return InteractionResult.CONSUME;
        }
        return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
    }

    @Override
    public void animateTick(BlockState state, Level level, BlockPos pos, RandomSource random) {
        if (state.getValue(HONEY) > 0 && random.nextInt(5) == 0) {
            Direction direction = Direction.getRandom(random);
            if (direction != Direction.UP) {
                BlockPos blockPos = pos.relative(direction);
                BlockState blockState = level.getBlockState(blockPos);
                if (!state.canOcclude() || !blockState.isFaceSturdy(level, blockPos, direction.getOpposite())) {
                    double xOffset = direction.getStepX() == 0 ? random.nextDouble() : 0.5 + (double) direction.getStepX() * 0.6;
                    double yOffset = direction.getStepY() == 0 ? random.nextDouble() : 0.5 + (double) direction.getStepY() * 0.6;
                    double zOffset = direction.getStepZ() == 0 ? random.nextDouble() : 0.5 + (double) direction.getStepZ() * 0.6;
                    level.addParticle(NarakaParticleTypes.DRIPPING_NECTARIUM.get(), pos.getX() + xOffset, pos.getY() + yOffset, pos.getZ() + zOffset, 0.0, 0.0, 0.0);
                }
            }
        }
    }

    private BlockPos findUpFaceSturdyBlock(ServerLevel level, BlockPos pos) {
        BlockPos.MutableBlockPos mutableBlockPos = pos.below(2).mutable();
        BlockState searchingState = level.getBlockState(mutableBlockPos);
        while (!searchingState.isFaceSturdy(level, mutableBlockPos, Direction.UP) && !searchingState.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK.get())) {
            if (pos.getY() - mutableBlockPos.getY() > MAX_HEIGHT)
                return mutableBlockPos.immutable();
            mutableBlockPos.move(Direction.DOWN);
            searchingState = level.getBlockState(mutableBlockPos);
        }

        return mutableBlockPos.immutable();
    }
}
