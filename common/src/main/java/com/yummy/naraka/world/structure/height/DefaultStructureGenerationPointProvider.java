package com.yummy.naraka.world.structure.height;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public class DefaultStructureGenerationPointProvider implements StructureGenerationPointProvider {
    @Override
    public Optional<BlockPos> getPoint(Structure.GenerationContext context, BlockPos base) {
        int height = context.chunkGenerator().getSeaLevel();

        return Optional.of(base.above(height));
    }
}
