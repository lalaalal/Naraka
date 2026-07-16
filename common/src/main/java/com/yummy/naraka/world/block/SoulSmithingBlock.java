package com.yummy.naraka.world.block;

import com.yummy.naraka.world.block.entity.ForgingBlockEntity;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.entity.SoulSmithingBlockEntity;
import com.yummy.naraka.world.item.NarakaItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class SoulSmithingBlock extends ForgingBlock {
    public static final EnumProperty<Direction> FACING = BlockStateProperties.HORIZONTAL_FACING;

    public SoulSmithingBlock(Properties properties) {
        super(properties);
        this.registerDefaultState(this.stateDefinition.any().setValue(FACING, Direction.NORTH));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(FACING);
    }

    @SuppressWarnings("deprecation")
    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return box(0, 0.1, 0, 16, 16, 16);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.defaultBlockState().setValue(FACING, context.getHorizontalDirection());
    }

    private boolean isStabilizerSide(BlockState state, Direction direction) {
        return state.getValue(FACING).getOpposite() == direction;
    }

    private boolean isTemplatedSide(BlockState state, Direction direction) {
        return state.getValue(FACING).getCounterClockWise() == direction;
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        ItemStack stack = player.getItemInHand(hand);
        if (stack.is(NarakaItems.NETHERITE_HAMMER.get()))
            return super.use(state, level, pos, player, hand, hitResult);
        if (blockEntity instanceof SoulSmithingBlockEntity soulSmithingBlockEntity) {
            if (isStabilizerSide(state, hitResult.getDirection())
                    && soulSmithingBlockEntity.isStabilizerAttached()) {
                soulSmithingBlockEntity.detachSoulStabilizer();
                return InteractionResult.SUCCESS;
            } else if (isTemplatedSide(state, hitResult.getDirection())
                    && !soulSmithingBlockEntity.getTemplateItem().isEmpty()) {
                soulSmithingBlockEntity.detachTemplateItem();
                return InteractionResult.SUCCESS;
            } else if (soulSmithingBlockEntity.tryAttachSoulStabilizer(stack)) {
                if (!player.isCreative())
                    stack.shrink(1);
                return InteractionResult.SUCCESS;
            } else if (soulSmithingBlockEntity.tryAttachTemplate(stack)) {
                if (!player.isCreative())
                    stack.shrink(1);
                return InteractionResult.SUCCESS;
            }
        }
        return super.use(state, level, pos, player, hand, hitResult);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new SoulSmithingBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide())
            return super.getTicker(level, state, type);
        return createTickerHelper(type, NarakaBlockEntityTypes.SOUL_SMITHING.get(), ForgingBlockEntity::serverTick);
    }
}
