package com.yummy.naraka.world.structure.generation;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Holder;

import java.util.Optional;

public class NarakaStructureGenerationPointProviders {
    public static final Holder<StructureGenerationPointProvider> NARAKA_PLATFORM = register(
            "naraka_platform", (context, base) -> Optional.of(base.atY(62))
    );

    public static final Holder<StructureGenerationPointProvider> HEROBRINE_SANCTUARY = register(
            "herobrine_sanctuary", new HerobrineSanctuaryGenerationPointProvider()
    );

    private static Holder<StructureGenerationPointProvider> register(String name, StructureGenerationPointProvider type) {
        return RegistryProxy.register(NarakaRegistries.Keys.STRUCTURE_GENERATION_POINT_PROVIDER, name, () -> type);
    }

    public static void initialize() {

    }
}
