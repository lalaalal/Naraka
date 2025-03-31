package com.yummy.naraka.fabric;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.Platform;
import com.yummy.naraka.core.registries.RegistryFactory;
import com.yummy.naraka.core.registries.RegistryInitializer;
import com.yummy.naraka.fabric.init.*;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.proxy.MethodInvoker;
import com.yummy.naraka.world.NarakaBiomes;
import net.fabricmc.api.ModInitializer;

public final class NarakaModFabric implements ModInitializer, NarakaInitializer {
    @Override
    public void onInitialize() {
        MethodInvoker.register(FabricNetworkManager.class);
        MethodInvoker.register(FabricEntityAttributeRegistry.class);
        MethodInvoker.register(FabricEventHandler.class);

        NarakaMod.initialize(this);
        NarakaMod.isRegistryLoaded = true;
    }

    @Override
    public Platform getPlatform() {
        return FabricPlatform.INSTANCE;
    }

    @Override
    public RegistryInitializer getRegistryInitializer() {
        return FabricRegistryInitializer.INSTANCE;
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runnable.run();
    }

    @Override
    public RegistryFactory getRegistryFactory() {
        return FabricRegistryFactory.INSTANCE;
    }

    @Override
    public NarakaBiomes.Modifier getBiomeModifier() {
        return FabricBiomeModifier.INSTANCE;
    }
}
