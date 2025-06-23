package com.yummy.naraka.world.block;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.NarakaEntityTypes;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.concurrent.atomic.AtomicInteger;

public class DiamondGolemSpawner extends Block {
    public static final IntegerProperty SPAWN_COUNT = IntegerProperty.create("spawn_count", 1, 3);

    public DiamondGolemSpawner(Properties properties) {
        super(properties);
        registerDefaultState(this.defaultBlockState().setValue(SPAWN_COUNT, 1));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(SPAWN_COUNT);
    }

    @Override
    protected void tick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        final int maxCount = state.getValue(SPAWN_COUNT);
        AtomicInteger count = new AtomicInteger();
        NarakaUtils.circle(pos, 4, NarakaUtils.OUTLINE, current -> {
            BlockPos floor = NarakaUtils.findFloor(level, current);
            if (count.get() < maxCount && !floor.equals(current)) {
                NarakaEntityTypes.DIAMOND_GOLEM.get().spawn(level, floor.above(), EntitySpawnReason.SPAWNER);
                count.incrementAndGet();
            }
        });
        level.destroyBlock(pos, false);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.INVISIBLE;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }
}
