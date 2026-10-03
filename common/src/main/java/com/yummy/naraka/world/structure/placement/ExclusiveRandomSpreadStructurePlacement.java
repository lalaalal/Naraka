package com.yummy.naraka.world.structure.placement;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.registries.codec.RegistryCodecs;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkGeneratorStructureState;
import net.minecraft.world.level.levelgen.LegacyRandomSource;
import net.minecraft.world.level.levelgen.WorldgenRandom;
import net.minecraft.world.level.levelgen.structure.StructureSet;
import net.minecraft.world.level.levelgen.structure.placement.AbstractSpreadingStructurePlacement;
import net.minecraft.world.level.levelgen.structure.placement.RandomSpreadType;

import java.util.Optional;

public class ExclusiveRandomSpreadStructurePlacement extends AbstractSpreadingStructurePlacement {
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
                    RegistryCodecs.holderSet(Registries.STRUCTURE_SET)
                            .fieldOf("exclusive_structures")
                            .forGetter(ExclusiveRandomSpreadStructurePlacement::exclusiveStructures),
                    Codec.intRange(0, 4096)
                            .fieldOf("exclusive_range")
                            .forGetter(ExclusiveRandomSpreadStructurePlacement::exclusiveRange),
                    Codec.INT.fieldOf("salt").forGetter(ExclusiveRandomSpreadStructurePlacement::salt)
            ).apply(instance, ExclusiveRandomSpreadStructurePlacement::new)
    );

    private final int spacing;
    private final int separation;
    private final RandomSpreadType spreadType;
    private final int minDistanceFromCenter;
    private final HolderSet<StructureSet> exclusiveStructures;
    private final int exclusiveRange;

    public ExclusiveRandomSpreadStructurePlacement(
            int minDistanceFromCenter,
            int spacing,
            int separation,
            RandomSpreadType spreadType,
            HolderSet<StructureSet> exclusiveStructures,
            int exclusiveRange,
            int salt) {
        super(Vec3i.ZERO, FrequencyReductionMethod.DEFAULT, 1, salt, Optional.empty());
        this.spacing = spacing;
        this.separation = separation;
        this.spreadType = spreadType;
        this.minDistanceFromCenter = minDistanceFromCenter;
        this.exclusiveStructures = exclusiveStructures;
        this.exclusiveRange = exclusiveRange;
    }

    public int spacing() {
        return spacing;
    }

    public int separation() {
        return separation;
    }

    public RandomSpreadType spreadType() {
        return spreadType;
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

    public ChunkPos getPotentialStructureChunk(final long seed, final int sourceX, final int sourceZ) {
        int spacedGridX = Math.floorDiv(sourceX, this.spacing);
        int spacedGridZ = Math.floorDiv(sourceZ, this.spacing);
        WorldgenRandom random = new WorldgenRandom(new LegacyRandomSource(seed));
        random.setLargeFeatureWithSalt(seed, spacedGridX, spacedGridZ, this.salt());
        int limit = this.spacing - this.separation;
        int spreadX = this.spreadType.evaluate(random, limit);
        int spreadZ = this.spreadType.evaluate(random, limit);
        return new ChunkPos(spacedGridX * this.spacing + spreadX, spacedGridZ * this.spacing + spreadZ);
    }

    @Override
    protected boolean isPlacementChunk(ChunkGeneratorStructureState structureState, int x, int z) {
        if (x * x + z * z < minDistanceFromCenter * minDistanceFromCenter)
            return false;
        for (Holder<StructureSet> exclusiveStructure : exclusiveStructures) {
            if (structureState.hasStructureChunkInRange(exclusiveStructure, x, z, exclusiveRange))
                return false;
        }
        ChunkPos chunkPos = this.getPotentialStructureChunk(structureState.getLevelSeed(), x, z);
        return chunkPos.x() == x && chunkPos.z() == z;
    }

    @Override
    public MapCodec<ExclusiveRandomSpreadStructurePlacement> codec() {
        return CODEC;
    }
}
