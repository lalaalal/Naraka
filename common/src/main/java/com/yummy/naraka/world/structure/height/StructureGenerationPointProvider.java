package com.yummy.naraka.world.structure.height;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.levelgen.structure.Structure;

import java.util.Optional;

public interface StructureGenerationPointProvider {
    Optional<BlockPos> getPoint(Structure.GenerationContext context, BlockPos base);
}
