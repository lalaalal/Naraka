package com.yummy.naraka.world.carver;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.chunk.CarvingMask;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.levelgen.Aquifer;
import net.minecraft.world.level.levelgen.carver.CarverConfiguration;
import net.minecraft.world.level.levelgen.carver.CarvingContext;
import net.minecraft.world.level.levelgen.carver.WorldCarver;

import java.util.function.Function;

public class YummyCarver extends WorldCarver<CarverConfiguration> {
    public YummyCarver() {
        super(CarverConfiguration.CODEC.codec());
    }

    @Override
    public boolean carve(
            CarvingContext context,
            CarverConfiguration config,
            ChunkAccess chunk,
            Function<BlockPos, Holder<Biome>> biomeAccessor,
            RandomSource random,
            Aquifer aquifer,
            ChunkPos chunkPos,
            CarvingMask carvingMask
    ) {
        int x = chunkPos.getBlockX(random.nextInt(16));
        int y = config.y.sample(random, context);
        int z = chunkPos.getBlockZ(random.nextInt(16));
        CarveSkipChecker skipChecker = (cc, relativeX, relativeY, relativeZ, minRelativeY) -> false;

        carveEllipsoid(context, config, chunk, biomeAccessor, aquifer, x, y, z, 32, 8, carvingMask, skipChecker);

        return false;
    }

    @Override
    public boolean isStartChunk(CarverConfiguration config, RandomSource random) {
        return random.nextFloat() <= config.probability;
    }
}
