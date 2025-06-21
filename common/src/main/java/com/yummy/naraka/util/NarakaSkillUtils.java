package com.yummy.naraka.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

import java.util.function.Supplier;

public class NarakaSkillUtils {
    public static void shockwaveBlocks(Level level, BlockPos base, int radius, Supplier<Vec3> movementSupplier) {
        base = NarakaUtils.findAir(level, base, Direction.UP);

        NarakaUtils.circle(base, radius, NarakaUtils.OUTLINE, blockPos -> {
            BlockPos floor = NarakaUtils.findFloor(level, blockPos);
            BlockState state = level.getBlockState(floor);
            NarakaEntityUtils.createFloatingBlock(level, floor.above(), state, movementSupplier.get());
        });
    }

    public static void shockwaveBlocks(Level level, BlockPos base, int radius) {
        shockwaveBlocks(level, base, radius, () -> new Vec3(0, 0.5, 0));
    }
}
