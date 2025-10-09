package com.yummy.naraka.world.structure.generation;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public class HerobrineSanctuaryGenerationPointProvider implements StructureGenerationPointProvider {
    @Override
    public Optional<BlockPos> getPoint(Structure.GenerationContext context, BlockPos base) {
        BlockPos comparingPosition = base.east(12);
        LevelHeightAccessor heightAccessor = context.heightAccessor();
        int seaLevel = context.chunkGenerator().getSeaLevel();
        int bridgeHeight1 = context.chunkGenerator().getFirstFreeHeight(base.getX(), base.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, context.randomState());
        int bridgeHeight2 = context.chunkGenerator().getFirstFreeHeight(comparingPosition.getX(), comparingPosition.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, context.randomState());
        if (Math.abs(bridgeHeight1 - bridgeHeight2) > 3)
            return Optional.empty();
        int heightOffset = Math.min(bridgeHeight1, bridgeHeight2);
        if (heightOffset > seaLevel + 3)
            return Optional.empty();
        if (heightOffset == seaLevel)
            heightOffset += 1;

        return Optional.of(base.above(heightOffset));
    }
}
