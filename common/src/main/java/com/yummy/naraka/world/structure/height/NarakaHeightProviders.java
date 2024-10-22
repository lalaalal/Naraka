package com.yummy.naraka.world.structure.height;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.core.registries.NarakaRegistries;
import dev.architectury.registry.registries.DeferredRegister;
import net.minecraft.core.Holder;

public class NarakaHeightProviders {
    private static final DeferredRegister<HeightProviderType> HEIGHT_PROVIDER_TYPES = DeferredRegister.create(NarakaMod.MOD_ID, NarakaRegistries.Keys.HEIGHT_PROVIDER_TYPE);

    public static final Holder<HeightProviderType> SEA_LEVEL_BASED = register(
            "sea_level_based", SeaLevelBasedHeightProvider::new
    );

    private static Holder<HeightProviderType> register(String name, HeightProviderType type) {
        return HEIGHT_PROVIDER_TYPES.register(name, () -> type);
    }

    public static void initialize() {
        HEIGHT_PROVIDER_TYPES.register();
    }
}
