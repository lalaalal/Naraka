package com.yummy.naraka.world.structure.piece;

import com.mojang.serialization.Codec;
import com.yummy.naraka.core.NarakaRegistries;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.resources.RegistryFixedCodec;
import net.minecraft.world.level.levelgen.structure.StructurePiece;

public interface StructurePieceFactory {
    Codec<Holder<StructurePieceFactory>> CODEC = RegistryFixedCodec.create(NarakaRegistries.STRUCTURE_PIECE_FACTORY);

    StructurePiece create(BlockPos pos);
}
