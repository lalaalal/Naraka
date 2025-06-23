package com.yummy.naraka.world.features;

import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.block.DiamondGolemSpawner;
import com.yummy.naraka.world.block.NarakaBlocks;
import com.yummy.naraka.world.features.configurations.OrePillarConfiguration;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import org.jetbrains.annotations.Nullable;

public class OrePillarFeature extends Feature<OrePillarConfiguration> {
    public OrePillarFeature() {
        super(OrePillarConfiguration.CODEC);
    }

    @Override
    public boolean place(FeaturePlaceContext<OrePillarConfiguration> context) {
        OrePillarConfiguration config = context.config();
        WorldGenLevel level = context.level();
        BlockPos origin = context.origin();
        OreSelector oreSelector = OreSelector.get(config.oreCandidates(), config.useSingleOre());

        BlockState state = level.getBlockState(origin);
        if (!state.isAir())
            return false;

        BlockPos bottom = NarakaUtils.findFloor(level, origin);
        BlockState floorState = level.getBlockState(bottom);
        if (bottom.equals(origin) || !floorState.is(BlockTags.OVERWORLD_CARVER_REPLACEABLES))
            return false;

        BlockPos top = NarakaUtils.findCeiling(level, bottom.above());
        int height = top.getY() - bottom.getY();
        if (config.maxHeight() < height || height < 7)
            return false;

        placePillar(level, context.random(), top, bottom, height, oreSelector, config);

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(context.random());
        BlockPos spawnerPos = origin.relative(direction, 2);

        int spawnCount = Mth.clamp(7 - config.maxHeight() / height, 1, 3);
        BlockState spawnerState = NarakaBlocks.DIAMOND_GOLEM_SPAWNER.get().defaultBlockState();
        level.setBlock(spawnerPos, spawnerState.setValue(DiamondGolemSpawner.SPAWN_COUNT, spawnCount), Block.UPDATE_ALL);
        level.scheduleTick(spawnerPos, NarakaBlocks.DIAMOND_GOLEM_SPAWNER.get(), 1);

        return true;
    }

    protected void placePillar(WorldGenLevel level, RandomSource random, BlockPos top, BlockPos bottom, int height, OreSelector oreSelector, OrePillarConfiguration config) {
        int radius = config.sampleRadius(random);
        placeSubPillar(level, random, bottom, oreSelector, config, radius, 1, height, Direction.UP, Direction.UP);
        placeSubPillar(level, random, top, oreSelector, config, radius, 1, height, Direction.DOWN, Direction.DOWN);
    }

    protected void placeSubPillar(WorldGenLevel level, RandomSource random, BlockPos start, OreSelector oreSelector, OrePillarConfiguration config,
                                  int maxRadius, int radius, int height, Direction spreadDirection, Direction growingDirection) {
        if (maxRadius <= radius || config.maxHeight() < height || height <= 1)
            return;

        BlockPos rootPos = NarakaUtils.findFaceSturdy(level, start, growingDirection);
        int offset = Math.abs(start.getY() - rootPos.getY());
        if (offset > radius + 2)
            rootPos = start.relative(growingDirection.getOpposite(), radius + 2);

        for (int y = 0; y < height + offset; y++) {
            BlockState state = selectBlockState(random, oreSelector, config);
            safeSetBlock(level, rootPos.relative(growingDirection, y), state, BlockBehaviour.BlockStateBase::canBeReplaced);
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            float scale = 0.67f * (height / (float) config.heightProvider().getMaxValue());
            int subPillarHeight = (int) Math.min(config.sampleHeight(random) * scale, height - 1);
            if (spreadDirection.getOpposite() != direction && random.nextFloat() < config.spreadChance())
                placeSubPillar(level, random, start.relative(direction), oreSelector, config, maxRadius, radius + 1, subPillarHeight, direction, growingDirection);
        }
    }

    protected BlockState selectBlockState(RandomSource random, OreSelector oreSelector, OrePillarConfiguration config) {
        if (random.nextFloat() < config.orePlaceChance())
            return oreSelector.selectOre(random);
        return config.baseBlock().value().defaultBlockState();
    }

    @FunctionalInterface
    protected interface OreSelector {
        static OreSelector get(HolderSet<Block> ores, boolean single) {
            return single ? single(ores) : mixed(ores);
        }

        static OreSelector single(HolderSet<Block> ores) {
            return new OreSelector() {
                private @Nullable BlockState result;

                @Override
                public BlockState selectOre(RandomSource random) {
                    if (result != null)
                        return result;
                    return result = select(random, ores);
                }
            };
        }

        static OreSelector mixed(HolderSet<Block> ores) {
            return random -> select(random, ores);
        }

        static BlockState select(RandomSource random, HolderSet<Block> ores) {
            return ores.getRandomElement(random)
                    .map(Holder::value)
                    .orElse(Blocks.AIR)
                    .defaultBlockState();
        }

        BlockState selectOre(RandomSource random);
    }
}
