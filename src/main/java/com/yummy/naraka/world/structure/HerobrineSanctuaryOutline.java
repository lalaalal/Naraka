package com.yummy.naraka.world.structure;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class HerobrineSanctuaryOutline extends StructurePiece {
    public static final int WIDTH = 20;
    public static final int HEIGHT = 1;
    public static final int DEPTH = 1;

    private final BlockPos pos;

    protected HerobrineSanctuaryOutline(BlockPos pos) {
        super(NarakaStructureTypes.HEROBRINE_SANCTUARY_OUTLINE.get(), 0, BoundingBox.fromCorners(pos, pos.offset(WIDTH, HEIGHT, DEPTH)));
        this.pos = pos;
    }

    public HerobrineSanctuaryOutline(StructurePieceSerializationContext context, CompoundTag tag) {
        super(NarakaStructureTypes.HEROBRINE_SANCTUARY_OUTLINE.get(), tag);
        pos = BlockPos.ZERO;
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext context, CompoundTag tag) {

    }

    @Override
    public void postProcess(WorldGenLevel level, StructureManager manager, ChunkGenerator generator, RandomSource random, BoundingBox boundingBox, ChunkPos chunkPos, BlockPos pos) {

    }
}
