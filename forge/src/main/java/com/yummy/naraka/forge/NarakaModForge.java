package com.yummy.naraka.forge;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.forge.client.NarakaModForgeClient;
import com.yummy.naraka.init.NarakaInitializer;
import com.yummy.naraka.invoker.MethodInvoker;
import com.yummy.naraka.forge.init.*;
import com.yummy.naraka.world.NarakaBiomes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

@Mod(NarakaMod.MOD_ID)
public final class NarakaModForge implements NarakaInitializer {
    @Nullable
    private static IEventBus MOD_BUS;

    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();
    private final NarakaModForgeClient narakaModForgeClient;

    public static IEventBus getModEventBus() {
        if (MOD_BUS == null)
            throw new IllegalStateException("Mod is not initialized!");
        return MOD_BUS;
    }

    public NarakaModForge(FMLJavaModLoadingContext context) {
        MOD_BUS = context.getModEventBus();
        this.narakaModForgeClient = new NarakaModForgeClient(MOD_BUS);
        MOD_BUS.addListener(narakaModForgeClient::clientSetup);

        MethodInvoker.register(ForgePlatform.class);
        MethodInvoker.register(ForgeNetworkManager.class);
        MethodInvoker.register(ForgeEventHandler.class);
        MethodInvoker.register(ForgeEntityAttributeRegistry.class);
        MethodInvoker.register(ForgeRegistryFactory.class);
        MethodInvoker.register(ForgeRegistryWriterProvider.class);
        MethodInvoker.register(ForgeCommandRegistry.class);
        MethodInvoker.register(ForgeEntityDataSerializerRegistry.class);

        NarakaMod.initialize(this);

        context.getModEventBus().addListener(this::commonSetup);
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runAfterRegistryLoaded.add(runnable);
    }

    @Override
    public NarakaBiomes.Modifier getBiomeModifier() {
        return ForgeBiomeModifier.INSTANCE;
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NarakaMod.isRegistryLoaded = true;
        for (Runnable runnable : runAfterRegistryLoaded)
            runnable.run();
    }
}
