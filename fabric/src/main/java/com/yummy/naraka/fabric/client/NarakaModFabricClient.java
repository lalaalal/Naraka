package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.init.NarakaClientInitializer;
import net.fabricmc.api.ClientModInitializer;

public final class NarakaModFabricClient implements ClientModInitializer, NarakaClientInitializer {
    @Override
    public void onInitializeClient() {
        NarakaModClient.initialize(this);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }
}
