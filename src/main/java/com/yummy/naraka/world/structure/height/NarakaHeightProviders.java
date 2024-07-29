package com.yummy.naraka.world.structure.height;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;

public class NarakaHeightProviders {
    public static final Holder<HeightProviderType> SEA_LEVEL_BASED = register(
            "sea_level_based", SeaLevelBasedHeightProvider::new
    );

    private static Holder<HeightProviderType> register(String name, HeightProviderType type) {
        return Registry.registerForHolder(NarakaRegistries.HEIGHT_PROVIDER_TYPE, NarakaMod.location(name), type);
    }

    public static void initialize() {

    }
}
