package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ResourceReloadListenerRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Supplier;

@Environment(EnvType.CLIENT)
public final class FabricResourceReloadListenerRegistry {
    @MethodProxy(value = ResourceReloadListenerRegistry.class, allowDelayedCall = true)
    public static void register(Identifier location, Supplier<PreparableReloadListener> listener) {
        ResourceLoader.get(PackType.CLIENT_RESOURCES)
                .registerReloader(location, listener.get());
    }
}
