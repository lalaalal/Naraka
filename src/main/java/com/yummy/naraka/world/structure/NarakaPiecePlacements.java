package com.yummy.naraka.world.structure;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.NarakaRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaPiecePlacements {
    private static final DeferredRegister<PiecePlacement> PIECE_PLACEMENTS = DeferredRegister.create(NarakaRegistries.PIECE_PLACEMENT, NarakaMod.MOD_ID);

    public static final DeferredHolder<PiecePlacement, HerobrineSanctuaryOutlinePlacement> HEROBRINE_SANCTUARY_OUTLINE = PIECE_PLACEMENTS.register(
            "herobrine_sanctuary_outline",
            HerobrineSanctuaryOutlinePlacement::new
    );

    public static void register(IEventBus bus) {
        PIECE_PLACEMENTS.register(bus);
    }
}
