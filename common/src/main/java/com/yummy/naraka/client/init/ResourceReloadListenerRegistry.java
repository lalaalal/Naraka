package com.yummy.naraka.client.init;

import com.yummy.naraka.client.NarakaClientServices;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Supplier;

public abstract class ResourceReloadListenerRegistry {
    public static void register(Identifier location, Supplier<PreparableReloadListener> listener) {
        NarakaClientServices.RESOURCE_RELOAD_LISTENER_REGISTRY.register(location, listener);
    }

    public interface Registrar {
        void register(Identifier location, Supplier<PreparableReloadListener> listener);
    }
}
