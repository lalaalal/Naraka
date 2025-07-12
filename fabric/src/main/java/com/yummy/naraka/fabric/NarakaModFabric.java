package com.yummy.naraka.fabric;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.fabric.init.*;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.invoker.MethodInvoker;
import com.yummy.naraka.world.NarakaBiomes;
import net.fabricmc.api.ModInitializer;

public final class NarakaModFabric implements ModInitializer, NarakaInitializer {
    @Override
    public void onInitialize() {
        MethodInvoker.register(FabricPlatform.class);
        MethodInvoker.register(FabricNetworkManager.class);
        MethodInvoker.register(FabricEntityAttributeRegistry.class);
        MethodInvoker.register(FabricEventHandler.class);
        MethodInvoker.register(FabricRegistryFactory.class);
        MethodInvoker.register(FabricRegistryProxyProvider.class);
        MethodInvoker.register(FabricSpawnPlacementRegistry.class);
        MethodInvoker.register(FabricCommandRegistry.class);

        NarakaMod.initialize(this);
        NarakaMod.isRegistryLoaded = true;
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }

    @Override
    public NarakaBiomes.Modifier getBiomeModifier() {
        return FabricBiomeModifier.INSTANCE;
    }
}
