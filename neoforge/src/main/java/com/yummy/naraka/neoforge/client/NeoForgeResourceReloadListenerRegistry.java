package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.ResourceReloadListenerRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

public final class NeoForgeResourceReloadListenerRegistry implements ResourceReloadListenerRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(Identifier location, Supplier<PreparableReloadListener> listener) {
        NARAKA_BUS.addListener((Consumer<AddClientReloadListenersEvent>) event -> {
            event.addListener(location, listener.get());
        });
    }
}
