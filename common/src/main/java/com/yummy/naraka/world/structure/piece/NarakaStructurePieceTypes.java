package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.core.registries.LazyHolder;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class NarakaStructurePieceTypes {
    public static final LazyHolder<StructurePieceType, StructurePieceType> HEROBRINE_SANCTUARY_OUTLINE = register(
            "herobrine_sanctuary_outline", HerobrineSanctuaryOutline::new
    );
    public static final LazyHolder<StructurePieceType, StructurePieceType> JUMBO_PIECE = register(
            "jumbo_piece", JumboPiece::new
    );

    private static LazyHolder<StructurePieceType, StructurePieceType> register(String name, StructurePieceType type) {
        return RegistryProxy.register(Registries.STRUCTURE_PIECE, name, () -> type);
    }

    public static void initialize() {
        RegistryInitializer.get(Registries.STRUCTURE_PIECE)
                .onRegistrationFinished();
    }
}
