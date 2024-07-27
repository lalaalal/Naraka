package com.yummy.naraka.world.trunkplacers;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class TrunkPlacerPatches {
    private static final Map<Block, Patch> PATCHES = new HashMap<>();

    public static void register(Block target, BlockState patchedState) {
        PATCHES.put(target, new Patch(patchedState));
    }

    public static boolean isRegistered(Block block) {
        return PATCHES.containsKey(block);
    }

    public static Function<BlockState, BlockState> get(Block target, Function<BlockState, BlockState> original) {
        if (PATCHES.containsKey(target))
            return PATCHES.get(target).setOriginal(original);
        throw new IllegalStateException();
    }

    private static class Patch implements Function<BlockState, BlockState> {
        private Function<BlockState, BlockState> original = Function.identity();
        private final BlockState patchedState;

        public Patch(BlockState patchedState) {
            this.patchedState = patchedState;
        }

        protected Patch setOriginal(Function<BlockState, BlockState> original) {
            this.original = original;
            return this;
        }

        @Override
        public BlockState apply(BlockState state) {
            BlockState newState = original.apply(state);
            if (newState.getValue(RotatedPillarBlock.AXIS) != Direction.Axis.Y)
                return patchedState;
            return newState;
        }
    }
}
