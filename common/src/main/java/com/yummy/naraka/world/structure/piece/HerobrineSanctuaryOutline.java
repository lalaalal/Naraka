package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.data.worldgen.NarakaStructures;
import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.WorldGenLevel;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.levelgen.structure.BoundingBox;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceSerializationContext;

public class HerobrineSanctuaryOutline extends StructurePiece {
    public static final float SPHERE_SIZE = 1.6f;
    private static final int STRUCTURE_WIDTH = 48 * 2 + 39;
    private static final int STRUCTURE_HEIGHT = 48 * 4;
    private static final int WIDTH = 48 * 6;
    private static final int HEIGHT = 48 * 6;
    private static final int DEPTH = 24;
    private static final int AIR_DEPTH = 48 * 4;

    private static final BlockPos OFFSET = new BlockPos(
            ((WIDTH - STRUCTURE_WIDTH) / -2) + 1, -DEPTH - 1, ((HEIGHT - STRUCTURE_HEIGHT) / -2) + 1
    ).offset(NarakaStructures.HEROBRINE_SANCTUARY_MAIN_OFFSET);

    private final BlockPos pos;
    private BoundingBox lavaBox;
    private BoundingBox airBox;

    private static BoundingBox createLavaBox(BlockPos pos) {
        return BoundingBox.fromCorners(pos, pos.offset(WIDTH, DEPTH, HEIGHT));
    }

    private static BoundingBox createAirBox(BlockPos pos) {
        pos = pos.offset(0, DEPTH, 0);
        return BoundingBox.fromCorners(pos, pos.offset(WIDTH, AIR_DEPTH, HEIGHT));
    }

    private static BoundingBox createBox(BlockPos pos) {
        return BoundingBox.fromCorners(pos, pos.offset(WIDTH, DEPTH + AIR_DEPTH, HEIGHT));
    }

    public HerobrineSanctuaryOutline(BlockPos pos) {
        super(NarakaStructurePieceTypes.HEROBRINE_SANCTUARY_OUTLINE.get(), 0, createBox(pos.offset(OFFSET)));
        this.pos = pos.offset(OFFSET);
        this.lavaBox = createLavaBox(this.pos);
        this.airBox = createAirBox(this.pos);
    }

    public HerobrineSanctuaryOutline(StructurePieceSerializationContext context, CompoundTag tag) {
        super(NarakaStructurePieceTypes.HEROBRINE_SANCTUARY_OUTLINE.get(), tag);
        this.pos = NbtUtils.readBlockPos(tag, "pos").orElse(BlockPos.ZERO);
        this.lavaBox = createLavaBox(pos);
        this.airBox = createAirBox(pos);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag tag) {
        tag.put("pos", NbtUtils.writeBlockPos(pos));
    }

    @Override
    public void move(int x, int y, int z) {
        super.move(x, y, z);
        airBox = airBox.moved(x, y, z);
        lavaBox = lavaBox.moved(x, y, z);
    }

    @Override
    public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator generator, RandomSource pRandom, BoundingBox pBox, ChunkPos pChunkPos, BlockPos pPos) {
        int seaLevel = generator.getSeaLevel();
        generateSphere(pLevel, pBox, boundingBox, airBox.minY(), airBox.maxY(), seaLevel, Blocks.AIR.defaultBlockState(), NarakaBlockTags.HEROBRINE_SANCTUARY_WRAP_TARGETS, Blocks.DIRT);
        generateSphere(pLevel, pBox, boundingBox, lavaBox.minY(), lavaBox.maxY(), seaLevel, Blocks.LAVA.defaultBlockState(), BlockTags.AIR, Blocks.STONE);
    }

    protected void generateSphere(WorldGenLevel level, BoundingBox boundingBox, BoundingBox box, int yStart, int yEnd, int seaLevel, BlockState state, TagKey<Block> wrapTarget, Block defaultReplace) {
        NarakaUtils.sphere(box, SPHERE_SIZE, (x, y, z) -> {
            if (yStart <= y && y <= yEnd) {
                wrap(level, boundingBox, box, x, y, z, seaLevel, wrapTarget, defaultReplace);
                placeBlock(level, state, x, y, z, boundingBox);
            }
        });
    }

    protected void wrap(WorldGenLevel level, BoundingBox boundingBox, BoundingBox box, int x, int y, int z, int seaLevel, TagKey<Block> target, Block defaultReplace) {
        int[] xOffsets = {0, 1, 0, -1};
        int[] zOffsets = {1, 0, -1, 0};
        for (int i = 0; i < xOffsets.length; i++) {
            int targetX = x + xOffsets[i];
            int targetZ = z + zOffsets[i];
            if (!NarakaUtils.isInSphere(box, SPHERE_SIZE, targetX, y, targetZ)) {
                BlockState state = getBlock(level, targetX, y, targetZ, boundingBox);
                if (state.is(target)) {
                    BlockState replace = findAppropriateState(y, seaLevel, defaultReplace);
                    placeBlock(level, replace, targetX, y, targetZ, boundingBox);
                }
            }
        }
    }

    protected BlockState refineReplaceable(BlockState state, int y, int seaLevel) {
        if (state.is(Blocks.DIRT) && y == seaLevel - 1)
            return Blocks.GRASS_BLOCK.defaultBlockState();
        return state;
    }

    protected BlockState findAppropriateState(int y, int seaLevel, Block defaultReplace) {
        if (y < seaLevel - 3)
            return Blocks.STONE.defaultBlockState();
        return refineReplaceable(defaultReplace.defaultBlockState(), y, seaLevel);
    }
}
