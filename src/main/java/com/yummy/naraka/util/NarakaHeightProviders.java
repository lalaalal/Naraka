package com.yummy.naraka.util;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.NarakaRegistries;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class NarakaHeightProviders {
    private static final DeferredRegister<HeightProviderType> HEIGHT_PROVIDER_TYPES = DeferredRegister.create(NarakaRegistries.HEIGHT_PROVIDER_TYPE, NarakaMod.MOD_ID);

    public static final DeferredHolder<HeightProviderType, HeightProviderType> SEA_LEVEL_BASED = HEIGHT_PROVIDER_TYPES.register(
            "sea_level_based",
            () -> SeaLevelBasedHeightProvider::new
    );

    public static void register(IEventBus bus) {
        HEIGHT_PROVIDER_TYPES.register(bus);
    }
}
