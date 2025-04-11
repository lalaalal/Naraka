package com.yummy.naraka.world.structure.piece;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import com.yummy.naraka.core.registries.RegistryProxyProvider;
import net.minecraft.core.Holder;

public class NarakaStructurePieceFactories {
    public static final Holder<StructurePieceFactory> HEROBRINE_SANCTUARY_OUTLINE = register(
            "herobrine_sanctuary_outline",
            HerobrineSanctuaryOutline::new
    );

    private static Holder<StructurePieceFactory> register(String name, StructurePieceFactory factory) {
        return RegistryProxy.register(NarakaRegistries.Keys.STRUCTURE_PIECE_FACTORY, name, () -> factory);
    }

    public static void initialize() {
        RegistryProxyProvider.get(NarakaRegistries.Keys.STRUCTURE_PIECE_FACTORY)
                .onRegistrationFinished();
    }
}
