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
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
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
    private static final MapCodec<SoulSmithingBlock> CODEC = simpleCodec(SoulSmithingBlock::new);
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

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
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
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        BlockEntity blockEntity = level.getBlockEntity(pos);
        if (stack.is(Items.MACE))
            return super.useItemOn(stack, state, level, pos, player, hand, hitResult);
        if (blockEntity instanceof SoulSmithingBlockEntity soulSmithingBlockEntity) {
            if (isStabilizerSide(state, hitResult.getDirection())
                    && soulSmithingBlockEntity.isStabilizerAttached()) {
                soulSmithingBlockEntity.detachSoulStabilizer();
                return ItemInteractionResult.SUCCESS;
            } else if (isTemplatedSide(state, hitResult.getDirection())
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
        if (level.isClientSide())
            return super.getTicker(level, state, type);
        return createTickerHelper(type, NarakaBlockEntityTypes.SOUL_SMITHING.get(), ForgingBlockEntity::serverTick);
    }
}
