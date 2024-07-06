package com.yummy.naraka.world.structure;

import com.yummy.naraka.data.worldgen.NarakaStructures;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class HerobrineSanctuaryOutline extends StructurePiece {
    private static final int STRUCTURE_WIDTH = 48 * 2 + 39;
    private static final int STRUCTURE_HEIGHT = 48 * 4;
    private static final int WIDTH = 48 * 4;
    private static final int HEIGHT = 48 * 5;
    private static final int DEPTH = 24;

    private static final BlockPos OFFSET = new BlockPos(
            ((WIDTH - STRUCTURE_WIDTH) / -2), -25, ((HEIGHT - STRUCTURE_HEIGHT) / -2)
    ).offset(NarakaStructures.HEROBRINE_SANCTUARY_MAIN_OFFSET);

    private final BlockPos pos;
    private final BoundingBox lavaBox;
    private final BoundingBox airBox;

    private static BoundingBox createLavaBox(BlockPos pos) {
        BlockPos start = pos.offset(OFFSET);
        return BoundingBox.fromCorners(start, start.offset(WIDTH, DEPTH, HEIGHT));
    }

    private static BoundingBox createAirBox(BlockPos pos) {
        BlockPos start = pos.offset(OFFSET).offset(0, DEPTH, 0);
        return BoundingBox.fromCorners(start, start.offset(WIDTH, DEPTH * 4, HEIGHT));
    }

    private static BoundingBox createBox(BlockPos pos) {
        BlockPos start = pos.offset(OFFSET);
        return BoundingBox.fromCorners(start, start.offset(WIDTH, DEPTH * 5, HEIGHT));
    }

    public HerobrineSanctuaryOutline(BlockPos pos) {
        super(NarakaStructureTypes.HEROBRINE_SANCTUARY_OUTLINE.get(), 0, createLavaBox(pos));
        this.pos = pos.offset(OFFSET);
        this.lavaBox = createLavaBox(pos);
        this.airBox = createAirBox(pos);
    }

    public HerobrineSanctuaryOutline(StructurePieceSerializationContext context, CompoundTag tag) {
        super(NarakaStructureTypes.HEROBRINE_SANCTUARY_OUTLINE.get(), tag);
        this.pos = NbtUtils.readBlockPos(tag, "pos").orElse(BlockPos.ZERO);
        this.lavaBox = createLavaBox(pos);
        this.airBox = createAirBox(pos);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag tag) {
        tag.put("pos", NbtUtils.writeBlockPos(pos));
    }

    @Override
    public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator pGenerator, RandomSource pRandom, BoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
        generateBox(pLevel, pBox, boundingBox, Blocks.LAVA.defaultBlockState(), Blocks.LAVA.defaultBlockState(), false);
    }
}
