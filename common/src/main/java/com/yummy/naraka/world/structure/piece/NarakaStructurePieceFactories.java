package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;

public class NarakaStructurePieceFactories {
    private static final DeferredRegister<StructurePieceFactory> STRUCTURE_PIECE_FACTORIES = DeferredRegister.create(NarakaMod.MOD_ID, NarakaRegistries.Keys.STRUCTURE_PIECE_FACTORY);

    public static final Holder<StructurePieceFactory> HEROBRINE_SANCTUARY_OUTLINE = register(
            "herobrine_sanctuary_outline",
            HerobrineSanctuaryOutline::new
    );

    private static Holder<StructurePieceFactory> register(String name, StructurePieceFactory factory) {
        return STRUCTURE_PIECE_FACTORIES.register(name, () -> factory);
    }

    public static void initialize() {
        STRUCTURE_PIECE_FACTORIES.register();
    }
}
