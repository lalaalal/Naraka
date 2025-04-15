package com.yummy.naraka.world.structure.height;

import com.yummy.naraka.core.registries.NarakaRegistries;
import com.yummy.naraka.core.registries.RegistryProxy;
import net.minecraft.core.Holder;

public class NarakaHeightProviders {
    public static final Holder<HeightProviderType> SEA_LEVEL_BASED = register(
            "sea_level_based", SeaLevelBasedHeightProvider::new
    );

    private static Holder<HeightProviderType> register(String name, HeightProviderType type) {
        return RegistryProxy.register(NarakaRegistries.Keys.HEIGHT_PROVIDER_TYPE, name, () -> type);
    }

    public static void initialize() {

    }
}
