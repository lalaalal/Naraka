package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.data.worldgen.NarakaStructures;
import com.yummy.naraka.tags.NarakaBlockTags;
import com.yummy.naraka.util.NarakaNbtUtils;
import com.yummy.naraka.util.NarakaUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
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
    public static final float SPHERE_SIZE = 0.6f;
    private static final int STRUCTURE_WIDTH = 48 * 2 + 9;
    private static final int STRUCTURE_HEIGHT = 48 * 2 + 29;
    private static final int WIDTH = 48 * 6 + 1;
    private static final int HEIGHT = 48 * 6 + 1;
    private static final int DEPTH = 48 * 2 + 24 + 1;
    private static final int AIR_DEPTH = 48 * 3 + 24;

    private static final BlockPos OFFSET = new BlockPos(
            ((WIDTH - STRUCTURE_WIDTH) / -2) - 1, -DEPTH + 7, ((HEIGHT - STRUCTURE_HEIGHT) / -2)
    ).offset(NarakaStructures.HEROBRINE_SANCTUARY_MAIN_OFFSET);

    private final BlockPos pos;
    private BoundingBox lavaBox;
    private BoundingBox airBox;

    private static BoundingBox createLavaBox(BlockPos pos) {
        return BoundingBox.fromCorners(pos, pos.offset(WIDTH, DEPTH - 1, HEIGHT));
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
        this.pos = NarakaNbtUtils.readBlockPos(tag, "pos").orElse(BlockPos.ZERO);
        this.lavaBox = createLavaBox(pos);
        this.airBox = createAirBox(pos);
    }

    @Override
    protected void addAdditionalSaveData(StructurePieceSerializationContext pContext, CompoundTag tag) {
        tag.put("pos", NarakaNbtUtils.writeBlockPos(pos));
    }

    @Override
    public void move(int x, int y, int z) {
        super.move(x, y, z);
        airBox = airBox.moved(x, y, z);
        lavaBox = lavaBox.moved(x, y, z);
    }

    @Override
    public void postProcess(WorldGenLevel pLevel, StructureManager pStructureManager, ChunkGenerator generator, RandomSource pRandom, BoundingBox processingBox, ChunkPos pChunkPos, BlockPos pPos) {
        int seaLevel = generator.getSeaLevel();
        generateSphere(pLevel, processingBox, boundingBox, airBox.minY(), airBox.maxY(), seaLevel, Blocks.AIR.defaultBlockState(), NarakaBlockTags.HEROBRINE_SANCTUARY_AIR_WRAP_TARGETS, Blocks.DIRT);
        generateSphere(pLevel, processingBox, boundingBox, lavaBox.minY(), lavaBox.maxY(), seaLevel, Blocks.LAVA.defaultBlockState(), NarakaBlockTags.HEROBRINE_SANCTUARY_LAVA_WRAP_TARGETS, Blocks.STONE);
    }

    protected void generateSphere(WorldGenLevel level, BoundingBox processingBox, BoundingBox box, int yStart, int yEnd, int seaLevel, BlockState state, TagKey<Block> wrapTarget, Block defaultReplace) {
        BoundingBox targetBox = BoundingBox.fromCorners(new Vec3i(processingBox.minX(), yStart, processingBox.minZ()), new Vec3i(processingBox.maxX(), yEnd, processingBox.maxZ()));
        NarakaUtils.sphere(box, targetBox, SPHERE_SIZE * 1.02f, (length, x, y, z) -> {
            if (length <= SPHERE_SIZE)
                placeBlock(level, state, x, y, z, processingBox);
            else {
                BlockState outlineState = getBlock(level, x, y, z, processingBox);
                if (outlineState.is(wrapTarget)) {
                    BlockState replace = findAppropriateState(y, seaLevel, defaultReplace);
                    placeBlock(level, replace, x, y, z, processingBox);
                }
            }
        });
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
