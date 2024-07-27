package com.yummy.naraka.world.trunkplacers;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.trunkplacers.FancyTrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;

import java.util.function.BiConsumer;
import java.util.function.Function;

public class EbonyTrunkPlacer extends FancyTrunkPlacer {
    public static final MapCodec<EbonyTrunkPlacer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> trunkPlacerParts(instance).apply(instance, EbonyTrunkPlacer::new)
    );

    public EbonyTrunkPlacer(int baseHeight, int heightRandA, int heightRandB) {
        super(baseHeight, heightRandA, heightRandB);
    }

    @Override
    protected TrunkPlacerType<?> type() {
        return NarakaTruncPlacerTypes.EBONY_TRUNK_PLACER;
    }

    @Override
    protected boolean placeLog(LevelSimulatedReader levelSimulatedReader, BiConsumer<BlockPos, BlockState> biConsumer, RandomSource randomSource, BlockPos blockPos, TreeConfiguration treeConfiguration, Function<BlockState, BlockState> function) {
        return super.placeLog(levelSimulatedReader, biConsumer, randomSource, blockPos, treeConfiguration, blockState -> rotatedLogPatcher(function, blockState));
    }

    private static BlockState rotatedLogPatcher(Function<BlockState, BlockState> function, BlockState state) {
        BlockState newState = function.apply(state);
        if (newState.getValue(RotatedPillarBlock.AXIS) != Direction.Axis.Y)
            return NarakaBlocks.EBONY_WOOD.defaultBlockState();
        return newState;
    }
}
