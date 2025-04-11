package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.core.registries.HolderProxy;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class NarakaStructurePieceTypes {
    public static final HolderProxy<StructurePieceType, StructurePieceType> HEROBRINE_SANCTUARY_OUTLINE = register(
            "herobrine_sanctuary_outline", HerobrineSanctuaryOutline::new
    );
    public static final HolderProxy<StructurePieceType, StructurePieceType> JUMBO_PIECE = register(
            "jumbo_piece", JumboPiece::new
    );

    private static HolderProxy<StructurePieceType, StructurePieceType> register(String name, StructurePieceType type) {
        return RegistryProxy.register(Registries.STRUCTURE_PIECE, name, () -> type);
    }

    public static void initialize() {
        RegistryProxyProvider.get(Registries.STRUCTURE_PIECE)
                .onRegistrationFinished();
    }
}
