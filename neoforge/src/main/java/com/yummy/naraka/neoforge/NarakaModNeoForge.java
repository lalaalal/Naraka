package com.yummy.naraka.neoforge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.neoforge.init.*;
import com.yummy.naraka.proxy.MethodInvoker;
import com.yummy.naraka.world.NarakaBiomes;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Mod(NarakaMod.MOD_ID)
public final class NarakaModNeoForge implements NarakaInitializer {
    @Nullable
    private static IEventBus MOD_BUS;

    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();

    public static IEventBus getModEventBus() {
        if (MOD_BUS == null)
            throw new IllegalStateException("Mod is not initialized!");
        return MOD_BUS;
    }

    public NarakaModNeoForge(IEventBus bus) {
        MOD_BUS = bus;

        MethodInvoker.register(NeoForgePlatform.class);
        MethodInvoker.register(NeoForgeNetworkManager.class);
        MethodInvoker.register(NeoForgeEventHandler.class);
        MethodInvoker.register(NeoForgeEntityAttributeRegistry.class);
        MethodInvoker.register(NeoForgeRegistryFactory.class);
        MethodInvoker.register(NeoForgeRegistryProxyProvider.class);

        NarakaMod.initialize(this);

        bus.addListener(this::commonSetup);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runAfterRegistryLoaded.add(runnable);
    }

    @Override
    public NarakaBiomes.Modifier getBiomeModifier() {
        return NeoForgeBiomeModifier.INSTANCE;
    }

    public void commonSetup(FMLCommonSetupEvent event) {
        NarakaMod.isRegistryLoaded = true;
        for (Runnable runnable : runAfterRegistryLoaded)
            runnable.run();
    }
}
