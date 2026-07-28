package com.yummy.naraka.forge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.Platform;
import com.yummy.naraka.forge.client.NarakaModForgeClient;
import com.yummy.naraka.init.NarakaInitializer;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

@Mod(NarakaMod.MOD_ID)
public final class NarakaModForge implements NarakaInitializer {
    @Nullable
    private static IEventBus MOD_BUS;

    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();

    public static IEventBus getModEventBus() {
        if (MOD_BUS == null)
            throw new IllegalStateException("Mod is not initialized!");
        return MOD_BUS;
    }

    public NarakaModForge(FMLJavaModLoadingContext context) {
        MOD_BUS = context.getModEventBus();

        NarakaMod.initialize(this);

        if (Platform.getInstance().getSide() == Platform.Side.CLIENT) {
            new NarakaModForgeClient(context);
        }

        context.getModEventBus().addListener(this::commonSetup);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runAfterRegistryLoaded.add(runnable);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NarakaMod.isRegistryLoaded = true;
        for (Runnable runnable : runAfterRegistryLoaded)
            runnable.run();
    }
}
