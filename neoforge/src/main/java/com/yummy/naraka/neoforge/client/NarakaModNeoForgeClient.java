package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.Platform;
import com.yummy.naraka.client.NarakaModClient;
import com.yummy.naraka.client.config.NarakaConfigScreen;
import com.yummy.naraka.client.init.NarakaClientInitializer;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.RegisterClientReloadListenersEvent;
import net.neoforged.neoforge.client.extensions.common.IClientItemExtensions;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
@Mod(value = NarakaMod.MOD_ID, dist = Dist.CLIENT)
public final class NarakaModNeoForgeClient implements NarakaClientInitializer, IClientItemExtensions {
    private final IEventBus bus;
    private final List<Runnable> runAfterRegistryLoaded = new ArrayList<>();

    public NarakaModNeoForgeClient(IEventBus modBus, ModContainer modContainer) {
        this.bus = modBus;

        NarakaModClient.initialize(this);
        if (Platform.getInstance().modExists("cloth_config"))
            modContainer.registerExtensionPoint(IConfigScreenFactory.class, (container, parent) -> NarakaConfigScreen.create(parent));

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
        bus.addListener((Consumer<RegisterClientReloadListenersEvent>) event -> {
            event.registerReloadListener(listener.get());
        });
    }
}
