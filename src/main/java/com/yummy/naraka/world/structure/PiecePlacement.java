package com.yummy.naraka.world.structure;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.NarakaRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructurePiece;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePiecesBuilder;

public abstract class PiecePlacement {
    public static final Codec<Holder<PiecePlacement>> CODEC = RegistryFixedCodec.create(NarakaRegistries.PIECE_PLACEMENT);

    protected abstract StructurePiece createPiece(BlockPos piecePos);

    public void place(Structure.GenerationContext context, StructurePiecesBuilder builder, BlockPos structureBasePos, BlockPos offset) {
        BlockPos piecePos = structureBasePos.offset(offset);
        builder.addPiece(createPiece(piecePos));
    }
}
