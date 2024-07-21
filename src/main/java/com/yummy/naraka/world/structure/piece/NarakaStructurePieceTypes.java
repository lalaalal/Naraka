package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class NarakaStructurePieceTypes {
    public static final StructurePieceType HEROBRINE_SANCTUARY_OUTLINE = register(
            "herobrine_sanctuary_outline", HerobrineSanctuaryOutline::new
    );
    public static final StructurePieceType JUMBO_PIECE = register(
            "jumbo_piece", JumboPiece::new
    );

    private static StructurePieceType register(String name, StructurePieceType type) {
        return Registry.register(BuiltInRegistries.STRUCTURE_PIECE, NarakaMod.location(name), type);
    }

    public static void initialize() {

    }
}
