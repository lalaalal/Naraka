package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.ResourceReloadListenerRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.PreparableReloadListener;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.AddClientReloadListenersEvent;

import java.util.function.Consumer;
import java.util.function.Supplier;

@OnlyIn(Dist.CLIENT)
public final class NeoForgeResourceReloadListenerRegistry implements NarakaEventBus {
    @MethodProxy(value = ResourceReloadListenerRegistry.class, allowDelayedCall = true)
    public static void register(ResourceLocation location, Supplier<PreparableReloadListener> listener) {
        NARAKA_BUS.addListener((Consumer<AddClientReloadListenersEvent>) event -> {
            event.addListener(location, listener.get());
        });
    }
}
