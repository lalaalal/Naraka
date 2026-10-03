package com.yummy.naraka.world.features;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.block.DiamondGolemSpawner;
import com.yummy.naraka.world.block.NarakaBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.core.registries.codec.RegistryFixedCodec;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.util.valueproviders.IntProvider;
import net.minecraft.util.valueproviders.IntProviders;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.feature.Feature;
import org.jspecify.annotations.Nullable;

public record OrePillarFeature(Holder<Block> baseBlock,
                               HolderSet<Block> oreCandidates,
                               int maxHeight,
                               IntProvider heightProvider,
                               IntProvider radiusProvider,
                               float orePlaceChance,
                               float spreadChance,
                               boolean useSingleOre) implements Feature {
    public static final MapCodec<OrePillarFeature> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    RegistryFixedCodec.create(Registries.BLOCK).fieldOf("base_block").forGetter(OrePillarFeature::baseBlock),
                    RegistryCodecs.holderSet(Registries.BLOCK).fieldOf("ore_candidates").forGetter(OrePillarFeature::oreCandidates),
                    Codec.INT.fieldOf("max_height").forGetter(OrePillarFeature::maxHeight),
                    IntProviders.CODEC.fieldOf("height_provider").forGetter(OrePillarFeature::heightProvider),
                    IntProviders.CODEC.fieldOf("radius_provider").forGetter(OrePillarFeature::radiusProvider),
                    Codec.FLOAT.fieldOf("ore_place_chance").forGetter(OrePillarFeature::orePlaceChance),
                    Codec.FLOAT.fieldOf("spread_chance").forGetter(OrePillarFeature::spreadChance),
                    Codec.BOOL.fieldOf("use_single_ore").forGetter(OrePillarFeature::useSingleOre)
            ).apply(instance, OrePillarFeature::new)
    );

    @Override
    public MapCodec<? extends Feature> codec() {
        return CODEC;
    }

    @Override
    public boolean place(WorldGenLevel level, ChunkGenerator chunkGenerator, RandomSource random, BlockPos origin) {
        OreSelector oreSelector = OreSelector.get(oreCandidates(), useSingleOre());

        BlockState state = level.getBlockState(origin);
        if (!state.isAir())
            return false;

        BlockPos bottom = NarakaUtils.findFloor(level, origin);
        BlockState floorState = level.getBlockState(bottom);
        if (bottom.equals(origin) || !floorState.is(BlockTags.STONE_ORE_REPLACEABLES))
            return false;

        BlockPos top = NarakaUtils.findCeiling(level, bottom.above());
        int height = top.getY() - bottom.getY();
        if (maxHeight() < height || height < 7)
            return false;

        placePillar(level, random, top, bottom, height, oreSelector);

        Direction direction = Direction.Plane.HORIZONTAL.getRandomDirection(random);
        BlockPos spawnerPos = origin.relative(direction, 2);

        BlockState spawnerState = NarakaBlocks.DIAMOND_GOLEM_SPAWNER.get().defaultBlockState();
        level.setBlock(spawnerPos, spawnerState.setValue(DiamondGolemSpawner.SPAWN_COUNT, 1), Block.UPDATE_ALL);
        level.scheduleTick(spawnerPos, NarakaBlocks.DIAMOND_GOLEM_SPAWNER.get(), 1);

        return true;
    }

    public int sampleHeight(RandomSource random) {
        return heightProvider.sample(random);
    }

    public int sampleRadius(RandomSource random) {
        return radiusProvider.sample(random);
    }

    private void placePillar(WorldGenLevel level, RandomSource random, BlockPos top, BlockPos bottom, int height, OreSelector oreSelector) {
        int radius = sampleRadius(random);
        placeSubPillar(level, random, bottom, oreSelector, radius, 1, height, Direction.UP, Direction.UP);
        placeSubPillar(level, random, top, oreSelector, radius, 1, height, Direction.DOWN, Direction.DOWN);
    }

    private void placeSubPillar(WorldGenLevel level, RandomSource random, BlockPos start, OreSelector oreSelector,
                                int maxRadius, int radius, int height, Direction spreadDirection, Direction growingDirection) {
        if (maxRadius <= radius || maxHeight() < height || height <= 1)
            return;

        BlockPos rootPos = NarakaUtils.findCollision(level, start, growingDirection);
        int offset = Math.abs(start.getY() - rootPos.getY());
        if (offset > radius + 2)
            rootPos = start.relative(growingDirection.getOpposite(), radius + 2);

        for (int y = 0; y < height + offset; y++) {
            BlockState state = selectBlockState(random, oreSelector);
            safeSetBlock(level, rootPos.relative(growingDirection, y), state, BlockBehaviour.BlockStateBase::canBeReplaced);
        }

        for (Direction direction : Direction.Plane.HORIZONTAL) {
            float scale = 0.67f * (height / (float) heightProvider().maxInclusive());
            int subPillarHeight = (int) Math.min(sampleHeight(random) * scale, height - 1);
            if (spreadDirection.getOpposite() != direction && random.nextFloat() < spreadChance())
                placeSubPillar(level, random, start.relative(direction), oreSelector, maxRadius, radius + 1, subPillarHeight, direction, growingDirection);
        }
    }

    private BlockState selectBlockState(RandomSource random, OreSelector oreSelector) {
        if (random.nextFloat() < orePlaceChance())
            return oreSelector.selectOre(random);
        return baseBlock().value().defaultBlockState();
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
