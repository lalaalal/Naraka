package com.yummy.naraka.world.block;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.tags.NarakaEntityTypeTags;
import com.yummy.naraka.world.block.entity.NarakaBlockEntityTypes;
import com.yummy.naraka.world.block.entity.UnstableBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

public class UnstableBlock extends BaseEntityBlock {
    public static final MapCodec<UnstableBlock> CODEC = simpleCodec(UnstableBlock::new);

    public static void makeUnstable(Level level, BlockPos pos) {
        BlockState state = level.getBlockState(pos);
        if (state.isFaceSturdy(level, pos, Direction.UP) && !state.is(NarakaBlocks.UNSTABLE_BLOCK.get())) {
            level.setBlock(pos, NarakaBlocks.UNSTABLE_BLOCK.get().defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            Optional<UnstableBlockEntity> unstableBlockEntity = level.getBlockEntity(pos, NarakaBlockEntityTypes.UNSTABLE_BLOCK.get());
            unstableBlockEntity.ifPresent(blockEntity -> blockEntity.setOriginal(state));
        }
    }

    public UnstableBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return box(0, 0.1, 0, 16, 16, 16);
    }

    @Override
    public void stepOn(Level level, BlockPos pos, BlockState state, Entity entity) {
        if (!entity.getType().is(NarakaEntityTypeTags.HEROBRINE)) {
            level.destroyBlock(pos, false);
            level.setBlock(pos.below(), Blocks.LAVA.defaultBlockState(), Block.UPDATE_ALL_IMMEDIATE);
            entity.igniteForSeconds(2);
        }
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new UnstableBlockEntity(pos, state);
    }
}
