package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.world.block.entity.HerobrineTotemBlockEntity;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import org.jetbrains.annotations.Nullable;

public class HerobrineTotem extends BaseEntityBlock {
    private static final MapCodec<HerobrineTotem> CODEC = simpleCodec(HerobrineTotem::new);

    public static final int MAX_CRACK = 9;
    public static final IntegerProperty CRACK = IntegerProperty.create("crack", 0, MAX_CRACK);

    private boolean placed = false;
    private BlockPos placedPos = BlockPos.ZERO;

    public static int light(BlockState state) {
        return state.getValue(CRACK);
    }

    public HerobrineTotem(Properties properties) {
        super(properties);
        this.registerDefaultState(
                getStateDefinition().any()
                        .setValue(CRACK, 0)
        );
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected void createBlockStateDefinition(Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CRACK);
    }

    @Override
    protected boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        return !placed || placedPos.equals(pos);
    }

    @Override
    protected void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean movedByPiston) {
        super.onPlace(state, level, pos, oldState, movedByPiston);
        placed = true;
        placedPos = pos;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        placed = false;
    }

    @SuppressWarnings("deprecation")
    @Override
    protected RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new HerobrineTotemBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level, BlockState state, BlockEntityType<T> type) {
        if (level.isClientSide)
            return super.getTicker(level, state, type);
        return createTickerHelper(type, NarakaBlockEntityTypes.HEROBRINE_TOTEM.get(), HerobrineTotemBlockEntity::serverTick);
    }
}
