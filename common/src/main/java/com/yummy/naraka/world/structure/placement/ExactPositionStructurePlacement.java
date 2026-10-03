package com.yummy.naraka.world.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;

public class ExactPositionStructurePlacement implements StructurePlacement {
    public static final MapCodec<ExactPositionStructurePlacement> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("chunk_x").forGetter(placement -> placement.chunkX),
                    Codec.INT.fieldOf("chunk_z").forGetter(placement -> placement.chunkZ)
            ).apply(instance, ExactPositionStructurePlacement::new)
    );

    private final int chunkX;
    private final int chunkZ;

    public ExactPositionStructurePlacement(int chunkX, int chunkZ) {
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    public boolean isStructureChunk(ChunkGeneratorStructureState state, int sourceX, int sourceZ) {
        return sourceX == chunkX && sourceZ == chunkZ;

    }

    @Override
    public MapCodec<? extends StructurePlacement> codec() {
        return CODEC;
    }
}
