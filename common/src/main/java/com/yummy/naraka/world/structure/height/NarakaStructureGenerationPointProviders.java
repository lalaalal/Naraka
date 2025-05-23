package com.yummy.naraka.world.structure.height;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Holder;

public class NarakaStructureGenerationPointProviders {
    public static final Holder<StructureGenerationPointProvider> SEA_LEVEL_HEIGHT = register(
            "sea_level_height", new DefaultStructureGenerationPointProvider()
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
