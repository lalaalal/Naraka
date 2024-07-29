package com.yummy.naraka.world.rootplacers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.world.level.LevelSimulatedReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import net.minecraft.world.level.levelgen.feature.rootplacers.AboveRootPlacement;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacer;
import net.minecraft.world.level.levelgen.feature.rootplacers.RootPlacerType;
import net.minecraft.world.level.levelgen.feature.stateproviders.BlockStateProvider;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class EbonyRootPlacer extends RootPlacer {
    public static final MapCodec<EbonyRootPlacer> CODEC = RecordCodecBuilder.mapCodec(
            instance -> rootPlacerParts(instance)
                    .and(IntProvider.codec(1, 4).fieldOf("sub_roots_count").forGetter(placer -> placer.subRootsCount))
                    .and(IntProvider.codec(0, 32).fieldOf("root_length").forGetter(placer -> placer.rootLength))
                    .and(Codec.floatRange(0, 1).fieldOf("skew_change").forGetter(placer -> placer.skewChance))
                    .apply(instance, EbonyRootPlacer::new)
    );

    protected final IntProvider subRootsCount;
    protected final IntProvider rootLength;
    protected final float skewChance;

    public EbonyRootPlacer(IntProvider trunkOffsetY, BlockStateProvider rootProvider, Optional<AboveRootPlacement> aboveRootPlacement, IntProvider subRootsCount, IntProvider rootLength, float skewChance) {
        super(trunkOffsetY, rootProvider, aboveRootPlacement);
        this.subRootsCount = subRootsCount;
        this.rootLength = rootLength;
        this.skewChance = skewChance;
    }

    @Override
    protected RootPlacerType<?> type() {
        return NarakaRootPlacerTypes.EBONY_ROOT;
    }

    @Override
    public boolean placeRoots(LevelSimulatedReader level, BiConsumer<BlockPos, BlockState> blockPlacer, RandomSource random, BlockPos pos1, BlockPos pos2, TreeConfiguration treeConfiguration) {
        int subRootsCount = this.subRootsCount.sample(random);
        EbonySubRoot[] subRoots = subRoots(random, subRootsCount);
        BlockPos mainRootPos = pos2.below();

        for (int i = 0; i < rootLength.sample(random); i++) {
            BlockPos pos = mainRootPos.below(i);
            blockPlacer.accept(pos, rootProvider.getState(random, pos));
        }

        for (EbonySubRoot subRoot : subRoots) {
            BlockPos rootStartPos = mainRootPos.relative(subRoot.mainDirection);
            subRoot.placeSubRoot(blockPlacer, random, rootStartPos);
        }

        return true;
    }

    private EbonySubRoot subRoot(RandomSource random, Direction main) {
        return new EbonySubRoot(main, Direction.Plane.HORIZONTAL.getRandomDirection(random));
    }

    private EbonySubRoot[] subRoots(RandomSource random, int count) {
        EbonySubRoot[] subRoots = new EbonySubRoot[count];
        List<Direction> directions = new ArrayList<>(List.of(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST));
        Collections.shuffle(directions);

        for (int index = 0; index < count; index++)
            subRoots[index] = subRoot(random, directions.get(index));

        return subRoots;
    }

    private class EbonySubRoot {
        private final Direction mainDirection;
        private final Optional<Direction> optionalDirection;

        public EbonySubRoot(Direction mainDirection, Direction optionalDirection) {
            this.mainDirection = mainDirection;
            if (optionalDirection == mainDirection)
                this.optionalDirection = Optional.empty();
            else this.optionalDirection = Optional.of(optionalDirection);
        }

        public void placeSubRoot(BiConsumer<BlockPos, BlockState> blockPlacer, RandomSource random, BlockPos start) {
            BlockPos.MutableBlockPos pos = start.mutable();
            BlockState rootState = rootProvider.getState(random, pos);
            int length = rootLength.sample(random);
            for (int i = 0; i < length; i++) {
                if (i > 0 && random.nextFloat() <= skewChance)
                    pos.move(mainDirection);
                if (i > 0 && optionalDirection.isPresent() && random.nextFloat() <= skewChance)
                    pos.move(optionalDirection.get());

                pos.move(Direction.DOWN);
                blockPlacer.accept(pos, rootState);
            }
        }
    }
}
