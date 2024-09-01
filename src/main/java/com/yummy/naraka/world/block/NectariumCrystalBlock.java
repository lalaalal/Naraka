package com.yummy.naraka.world.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DripstoneThickness;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class NectariumCrystalBlock extends Block {
    public static final DirectionProperty TIP_DIRECTION = BlockStateProperties.VERTICAL_DIRECTION;
    public static final EnumProperty<DripstoneThickness> THICKNESS = BlockStateProperties.DRIPSTONE_THICKNESS;

    private static final VoxelShape TIP_MERGE_SHAPE = Block.box(5.0, 0.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape TIP_SHAPE_UP = Block.box(5.0, 0.0, 5.0, 11.0, 11.0, 11.0);
    private static final VoxelShape TIP_SHAPE_DOWN = Block.box(5.0, 5.0, 5.0, 11.0, 16.0, 11.0);
    private static final VoxelShape FRUSTUM_SHAPE = Block.box(4.0, 0.0, 4.0, 12.0, 16.0, 12.0);
    private static final VoxelShape MIDDLE_SHAPE = Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0);
    private static final VoxelShape BASE_SHAPE = Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);

    public NectariumCrystalBlock(Properties properties) {
        super(properties);
        registerDefaultState(
                this.stateDefinition.any()
                        .setValue(TIP_DIRECTION, Direction.UP)
                        .setValue(THICKNESS, DripstoneThickness.TIP)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(TIP_DIRECTION, THICKNESS);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return switch (state.getValue(THICKNESS)) {
            case TIP_MERGE -> TIP_MERGE_SHAPE;
            case TIP -> state.getValue(TIP_DIRECTION) == Direction.UP ? TIP_SHAPE_UP : TIP_SHAPE_DOWN;
            case FRUSTUM -> FRUSTUM_SHAPE;
            case MIDDLE -> MIDDLE_SHAPE;
            case BASE -> BASE_SHAPE;
        };
    }

    @Override
    protected BlockState updateShape(BlockState state, Direction direction, BlockState neighborState, LevelAccessor level, BlockPos pos, BlockPos neighborPos) {
        if (state.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK)
                && !canSurvive(state, level, pos)
                && !level.getBlockTicks().hasScheduledTick(pos, this))
            level.scheduleTick(pos, this, 1);
        Direction tipDirection = state.getValue(TIP_DIRECTION);
        if (direction == tipDirection) {
            DripstoneThickness thickness = calculateThickness(level, state, neighborState, pos);
            return state.setValue(THICKNESS, thickness);
        }
        return state;
    }

    protected DripstoneThickness calculateThickness(LevelAccessor level, BlockState state, BlockState neighborState, BlockPos pos) {
        Direction tipDirection = state.getValue(TIP_DIRECTION);
        BlockPos basePos = pos.relative(tipDirection.getOpposite());
        BlockState baseState = level.getBlockState(basePos);

        if (!neighborState.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK))
            return DripstoneThickness.TIP;
        DripstoneThickness thickness = neighborState.getValue(THICKNESS);

        if (canMerge(level, pos, tipDirection))
            return DripstoneThickness.TIP_MERGE;
        if (baseState.isFaceSturdy(level, basePos, tipDirection))
            return thicker(thickness);
        if (baseState.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK))
            return thickerUntilMiddle(thickness);
        return thickness;
    }

    protected boolean canMerge(LevelAccessor level, BlockPos pos, Direction tipDirection) {
        BlockPos headPos = pos.relative(tipDirection);
        BlockState headState = level.getBlockState(headPos);
        return headState.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK)
                && headState.getValue(TIP_DIRECTION).getOpposite() == tipDirection
                && (headState.getValue(THICKNESS) == DripstoneThickness.TIP || headState.getValue(THICKNESS) == DripstoneThickness.TIP_MERGE);
    }

    protected static DripstoneThickness thicker(DripstoneThickness thickness) {
        return switch (thickness) {
            case TIP_MERGE, TIP -> DripstoneThickness.FRUSTUM;
            case FRUSTUM -> DripstoneThickness.MIDDLE;
            case MIDDLE, BASE -> DripstoneThickness.BASE;
        };
    }

    protected static DripstoneThickness thickerUntilMiddle(DripstoneThickness thickness) {
        return switch (thickness) {
            case TIP_MERGE, TIP -> DripstoneThickness.FRUSTUM;
            case FRUSTUM, MIDDLE -> DripstoneThickness.MIDDLE;
            case BASE -> DripstoneThickness.BASE;
        };
    }

    @Override
    public @Nullable BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Direction clickedDirection = context.getNearestLookingVerticalDirection().getOpposite();
        BlockPos clickedPos = pos.relative(clickedDirection.getOpposite());
        BlockState state = level.getBlockState(clickedPos);
        Direction tipDirection = calculateTipDirection(level, clickedDirection, clickedPos, state);
        if (tipDirection == null)
            return null;
        DripstoneThickness thickness = DripstoneThickness.TIP;
        if (canMerge(level, pos, tipDirection))
            thickness = DripstoneThickness.TIP_MERGE;
        return defaultBlockState()
                .setValue(TIP_DIRECTION, tipDirection)
                .setValue(THICKNESS, thickness);
    }

    protected @Nullable Direction calculateTipDirection(Level level, Direction clickedDirection, BlockPos clickedPos, BlockState state) {
        if (state.isFaceSturdy(level, clickedPos, clickedDirection))
            return clickedDirection;
        if (state.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK))
            return state.getValue(TIP_DIRECTION);
        return null;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (!canSurvive(state, level, pos))
            level.destroyBlock(pos, true);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        Direction tipDirection = state.getValue(TIP_DIRECTION);
        BlockPos basePos = pos.relative(tipDirection.getOpposite());
        BlockState baseState = level.getBlockState(basePos);
        return baseState.is(NarakaBlocks.NECTARIUM_CRYSTAL_BLOCK) || baseState.isFaceSturdy(level, basePos, tipDirection);
    }
}
