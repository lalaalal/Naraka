package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.init.NarakaClientInitializer;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.minecraftforge.client.event.RegisterClientReloadListenersEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NarakaModForgeClient implements NarakaClientInitializer {
    private final IEventBus bus;
    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();

    public NarakaModForgeClient(FMLJavaModLoadingContext context) {
        this.bus = context.getModEventBus();

        MethodInvoker.register(ForgeClientEventHandler.class);
        MethodInvoker.register(ForgeModelLayerRegistry.class);
        MethodInvoker.register(ForgeParticleProviderRegistry.class);
        MethodInvoker.register(ForgeBlockEntityRendererRegistry.class);
        MethodInvoker.register(ForgeEntityRendererRegistry.class);
        MethodInvoker.register(ForgeScreenFactoryRegistry.class);
        MethodInvoker.register(ForgeHudRendererRegistry.class);
        MethodInvoker.register(ForgeKeyMappingRegistry.class);
        MethodInvoker.register(ForgeShaderRegistry.class);
        MethodInvoker.register(ForgeItemPropertiesRegistry.class);
        MethodInvoker.register(ForgeDimensionSpecialEffectsRegistry.class);
        MethodInvoker.register(ForgeClientNetworkManager.class);

        NarakaModClient.initialize(this);

        bus.addListener(this::clientSetup);
    }

    public void clientSetup(FMLClientSetupEvent event) {
        for (Runnable runnable : runAfterRegistryLoaded)
            runnable.run();
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runAfterRegistryLoaded.add(runnable);
    }

    @Override
    public void registerClientReloadListener(String name, Supplier<PreparableReloadListener> listener) {
        bus.addListener((Consumer<RegisterClientReloadListenersEvent>) event -> {
            event.registerReloadListener(listener.get());
        });
    }
}