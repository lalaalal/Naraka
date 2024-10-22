package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.NarakaMod;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;

public class NarakaStructurePieceTypes {
    private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECE_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, Registries.STRUCTURE_PIECE);

    public static final RegistrySupplier<StructurePieceType> HEROBRINE_SANCTUARY_OUTLINE = register(
            "herobrine_sanctuary_outline", HerobrineSanctuaryOutline::new
    );
    public static final RegistrySupplier<StructurePieceType> JUMBO_PIECE = register(
            "jumbo_piece", JumboPiece::new
    );

    private static RegistrySupplier<StructurePieceType> register(String name, StructurePieceType type) {
        return STRUCTURE_PIECE_TYPES.register(name, () -> type);
    }

    public static void initialize() {
        STRUCTURE_PIECE_TYPES.register();
    }
}
