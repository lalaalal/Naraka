package com.yummy.naraka.world.structure;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.NarakaRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaStructurePieceFactories {
    private static final DeferredRegister<StructurePieceFactory> STRUCTURE_PIECE_FACTORIES = DeferredRegister.create(NarakaRegistries.STRUCTURE_PIECE_FACTORY, NarakaMod.MOD_ID);

    public static final DeferredHolder<StructurePieceFactory, StructurePieceFactory> HEROBRINE_SANCTUARY_OUTLINE = STRUCTURE_PIECE_FACTORIES.register(
            "herobrine_sanctuary_outline",
            () -> HerobrineSanctuaryOutline::new
    );

    public static void register(IEventBus bus) {
        STRUCTURE_PIECE_FACTORIES.register(bus);
    }
}
