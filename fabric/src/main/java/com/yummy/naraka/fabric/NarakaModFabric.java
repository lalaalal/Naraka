package com.yummy.naraka.fabric;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.NarakaInitializer;
import net.fabricmc.api.ModInitializer;

public final class NarakaModFabric implements ModInitializer, NarakaInitializer {
    @Override
    public void onInitialize() {
        NarakaMod.initialize(this);
        NarakaMod.isRegistryLoaded = true;
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }
}
