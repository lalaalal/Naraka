package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.init.NarakaClientInitializer;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Mod(value = NarakaMod.MOD_ID, dist = Dist.CLIENT)
public final class NarakaModNeoForgeClient implements NarakaClientInitializer, IClientItemExtensions {
    private final IEventBus bus;
    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();

    public NarakaModNeoForgeClient(IEventBus modBus) {
        this.bus = modBus;

        MethodInvoker.register(NeoForgeClientEventHandler.class);
        MethodInvoker.register(NeoForgeModelLayerRegistry.class);
        MethodInvoker.register(NeoForgeParticleProviderRegistry.class);
        MethodInvoker.register(NeoForgeBlockEntityRendererRegistry.class);
        MethodInvoker.register(NeoForgeEntityRendererRegistry.class);
        MethodInvoker.register(NeoForgeScreenFactoryRegistry.class);
        MethodInvoker.register(NeoForgeHudRendererRegistry.class);
        MethodInvoker.register(NeoForgeKeyMappingRegistry.class);
        MethodInvoker.register(NeoForgeRenderPipelineRegistry.class);

        NarakaModClient.initialize(this);

        modBus.addListener(this::clientSetup);
    }

    private void clientSetup(FMLClientSetupEvent event) {
        for (Runnable runnable : runAfterRegistryLoaded)
            runnable.run();
    }

    @Override
    public void runAfterRegistryLoaded(Runnable runnable) {
        runAfterRegistryLoaded.add(runnable);
    }

    @Override
    public void registerClientReloadListener(String name, Supplier<PreparableReloadListener> listener) {
        bus.addListener((Consumer<AddClientReloadListenersEvent>) event -> {
            event.addListener(NarakaMod.location(name), listener.get());
        });
    }
}