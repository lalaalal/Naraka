package com.yummy.naraka.world.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;
import net.minecraft.world.level.levelgen.structure.placement.StructurePlacementType;

public class ExclusiveRandomSpreadStructurePlacement extends RandomSpreadStructurePlacement {
    public static final MapCodec<ExclusiveRandomSpreadStructurePlacement> CODEC = RecordCodecBuilder.mapCodec(
            instance -> instance.group(
                    Codec.intRange(0, 4096)
                            .fieldOf("min_distance_from_center")
                            .forGetter(ExclusiveRandomSpreadStructurePlacement::minDistanceFromCenter),
                    Codec.intRange(0, 4096)
                            .fieldOf("spacing")
                            .forGetter(ExclusiveRandomSpreadStructurePlacement::spacing),
                    Codec.intRange(0, 4096)
                            .fieldOf("separation")
                            .forGetter(ExclusiveRandomSpreadStructurePlacement::separation),
                    RandomSpreadType.CODEC
                            .optionalFieldOf("spread_type", RandomSpreadType.LINEAR)
                            .forGetter(ExclusiveRandomSpreadStructurePlacement::spreadType),
                    RegistryCodecs.homogeneousList(Registries.STRUCTURE_SET)
                            .fieldOf("exclusive_structures")
                            .forGetter(ExclusiveRandomSpreadStructurePlacement::exclusiveStructures),
                    Codec.intRange(0, 4096)
                            .fieldOf("exclusive_range")
                            .forGetter(ExclusiveRandomSpreadStructurePlacement::exclusiveRange),
                    Codec.INT.fieldOf("salt").forGetter(ExclusiveRandomSpreadStructurePlacement::salt)
            ).apply(instance, ExclusiveRandomSpreadStructurePlacement::new)
    );

    private final int minDistanceFromCenter;
    private final HolderSet<StructureSet> exclusiveStructures;
    private final int exclusiveRange;

    public ExclusiveRandomSpreadStructurePlacement(int minDistanceFromCenter, int spacing, int separation, RandomSpreadType spreadType, HolderSet<StructureSet> exclusiveStructures, int exclusiveRange, int salt) {
        super(spacing, separation, spreadType, salt);
        this.minDistanceFromCenter = minDistanceFromCenter;
        this.exclusiveStructures = exclusiveStructures;
        this.exclusiveRange = exclusiveRange;
    }

    public int minDistanceFromCenter() {
        return minDistanceFromCenter;
    }

    public HolderSet<StructureSet> exclusiveStructures() {
        return exclusiveStructures;
    }

    public int exclusiveRange() {
        return exclusiveRange;
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        if (x < minDistanceFromCenter || z < minDistanceFromCenter)
            return false;
        for (Holder<StructureSet> exclusiveStructure : exclusiveStructures) {
            if (structureState.hasStructureChunkInRange(exclusiveStructure, x, z, exclusiveRange))
                return false;
        }
        return super.isPlacementChunk(structureState, x, z);
    }

    @Override
    public StructurePlacementType<?> type() {
        return NarakaStructurePlacementTypes.EXCLUSIVE_RANDOM_SPREAD.get();
    }
}
