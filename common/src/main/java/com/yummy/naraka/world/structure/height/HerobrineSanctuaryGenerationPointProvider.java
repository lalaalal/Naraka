package com.yummy.naraka.world.structure.height;

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
        int bridgeHeight1 = context.chunkGenerator().getFirstFreeHeight(base.getX(), base.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, context.randomState());
        int bridgeHeight2 = context.chunkGenerator().getFirstFreeHeight(comparingPosition.getX(), comparingPosition.getZ(), Heightmap.Types.WORLD_SURFACE_WG, heightAccessor, context.randomState());
        if (Math.abs(bridgeHeight1 - bridgeHeight2) > 2)
            return Optional.empty();
        int heightOffset = Math.min(bridgeHeight1, bridgeHeight2);

        return Optional.of(base.above(heightOffset));
    }
}
