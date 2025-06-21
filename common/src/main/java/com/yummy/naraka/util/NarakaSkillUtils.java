package com.yummy.naraka.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class NarakaSkillUtils {
    public static void shockwaveBlocks(Level level, BlockPos base, int radius) {
        base = NarakaUtils.findAir(level, base, Direction.UP);

        NarakaUtils.circle(base, radius, NarakaUtils.OUTLINE, blockPos -> {
            BlockPos floor = NarakaUtils.findFloor(level, blockPos);
            BlockState state = level.getBlockState(floor);
            FallingBlockEntity fallingBlockEntity = NarakaEntityUtils.createFloatingBlock(level, floor, state);
            fallingBlockEntity.setDeltaMovement(0, 0.5, 0);
        });
    }
}
