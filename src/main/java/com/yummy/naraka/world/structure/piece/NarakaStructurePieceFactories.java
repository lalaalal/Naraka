package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

public class NarakaStructurePieceFactories {
    public static final Holder<StructurePieceFactory> HEROBRINE_SANCTUARY_OUTLINE = register(
            "herobrine_sanctuary_outline",
            HerobrineSanctuaryOutline::new
    );

    private static Holder<StructurePieceFactory> register(String name, StructurePieceFactory factory) {
        return Registry.registerForHolder(NarakaRegistries.STRUCTURE_PIECE_FACTORY, NarakaMod.location(name), factory);
    }

    public static void initialize() {

    }
}
