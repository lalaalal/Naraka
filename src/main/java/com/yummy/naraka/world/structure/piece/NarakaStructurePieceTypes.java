package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.NarakaMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaStructurePieceTypes {

    private static final DeferredRegister<StructurePieceType> STRUCTURE_PIECES = DeferredRegister.create(Registries.STRUCTURE_PIECE, NarakaMod.MOD_ID);
    public static final DeferredHolder<StructurePieceType, StructurePieceType> HEROBRINE_SANCTUARY_OUTLINE = STRUCTURE_PIECES.register(
            "herobrine_sanctuary_outline",
            () -> HerobrineSanctuaryOutline::new
    );
    public static final DeferredHolder<StructurePieceType, StructurePieceType> JUMBO_PIECE = STRUCTURE_PIECES.register(
            "jumbo_piece",
            () -> JumboPiece::new
    );

    public static void register(IEventBus bus) {
        STRUCTURE_PIECES.register(bus);
    }
}
