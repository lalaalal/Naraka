package com.yummy.naraka.world.structure;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;

import java.util.Optional;

public class HerobrineSanctuaryStructure extends Structure {
    public static final MapCodec<HerobrineSanctuaryStructure> CODEC = simpleCodec(HerobrineSanctuaryStructure::new);

    public HerobrineSanctuaryStructure(StructureSettings settings) {
        super(settings);
    }

    @Override
    protected Optional<GenerationStub> findGenerationPoint(GenerationContext context) {
        ChunkPos chunkPos = context.chunkPos();
        ChunkGenerator chunkGenerator = context.chunkGenerator();
        int height = chunkGenerator.getFirstFreeHeight(chunkPos.x, chunkPos.z,
                Heightmap.Types.WORLD_SURFACE_WG,
                context.heightAccessor(),
                context.randomState()
        );
        BlockPos base = new BlockPos(chunkPos.getMinBlockX(), height, chunkPos.getMinBlockZ());
        return HerobrineSanctuaryPlacement.addPieces(context, base, new BlockPos(0, -12, 0));
    }

    @Override
    public StructureType<?> type() {
        return NarakaStructureTypes.HEROBRINE_SANCTUARY.get();
    }
}
