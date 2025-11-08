package com.yummy.naraka.world.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Vec3i;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

import java.util.Optional;

public class ExactPositionStructurePlacement extends StructurePlacement {
    public static final MapCodec<ExactPositionStructurePlacement> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.INT.fieldOf("chunk_x").forGetter(placement -> placement.chunkX),
                    Codec.INT.fieldOf("chunk_z").forGetter(placement -> placement.chunkZ)
            ).apply(instance, ExactPositionStructurePlacement::new)
    );

    private final int chunkX;
    private final int chunkZ;

    public ExactPositionStructurePlacement(int chunkX, int chunkZ) {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1, 1, Optional.empty());
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        return x == chunkX && z == chunkZ;
    }

    @Override
    public StructurePlacementType<?> type() {
        return NarakaStructurePlacementTypes.EXACT_POSITION.get();
    }
}
