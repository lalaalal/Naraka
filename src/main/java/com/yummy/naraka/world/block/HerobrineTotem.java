package com.yummy.naraka.world.block;

import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.NarakaEntityTypes;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition.Builder;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;

public class HerobrineTotem extends Block {
    public static final int MAX_CRACK = 5;
    public static final IntegerProperty CRACK = IntegerProperty.create("crack", 0, MAX_CRACK);

    private boolean placed = false;
    private BlockPos placedPos = BlockPos.ZERO;
    private int tickCount = 1;

    public static int light(BlockState ignored) {
        return 7;
    }

    public HerobrineTotem(Properties properties) {
        super(properties);
        this.registerDefaultState(
                getStateDefinition().any()
                        .setValue(CRACK, 0)
        );
    }

    private boolean isValidState(BlockState state, BlockPos pos) {
        return placed && placedPos.equals(pos);
    }

    public boolean isActivated(BlockState state, BlockPos pos) {
        return isValidState(state, pos) && 0 < state.getValue(CRACK);
    }

    public boolean isSleeping(BlockState state, BlockPos pos) {
        return !isActivated(state, pos);
    }

    public void crack(BlockState state, BlockPos pos) {
        int crack = state.getValue(CRACK);
        if (isValidState(state, pos) && crack < MAX_CRACK)
            state.setValue(CRACK, crack + 1);
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
        tickCount = 1;
    }

    @Override
    protected void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean movedByPiston) {
        super.onRemove(state, level, pos, newState, movedByPiston);
        placed = false;
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        super.tick(state, level, pos, random);
        if (isSleeping(state, pos)) {
            if (isTotemStructure(level, pos) && refineFire(level, pos)) {
                crack(state, pos);
            }
            return;
        }

        if (tickCount % 20 == 0) {
            if (state.getValue(CRACK) == MAX_CRACK) {
                summonHerobrine(state, level, pos);
                breakTotemStructure(level, pos);
            } else
                crack(state, pos);
        }

        tickCount += 1;
    }

    public boolean isTotemStructure(ServerLevel level, BlockPos totemPos) {
        return level.getBlockState(totemPos.above()).is(Blocks.NETHERRACK)
                && level.getBlockState(totemPos.below(1)).is(NarakaBlocks.FAKE_GOLD_BLOCK)
                && level.getBlockState(totemPos.below(2)).is(NarakaBlocks.FAKE_GOLD_BLOCK);
    }

    public boolean refineFire(ServerLevel level, BlockPos totemPos) {
        if (level.getBlockState(totemPos.above(2)).is(BlockTags.FIRE)) {
            level.setBlock(totemPos, NarakaBlocks.PURIFIED_SOUL_FIRE_BLOCK.get().defaultBlockState(), UPDATE_CLIENTS);
            return true;
        }
        return false;
    }

    private void summonHerobrine(BlockState state, ServerLevel level, BlockPos pos) {
        Herobrine herobrine = new Herobrine(level, new Vec3(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
        level.addFreshEntity(herobrine);
    }

    private void breakTotemStructure(ServerLevel level, BlockPos pos) {
        level.destroyBlock(pos, false);
    }
}
