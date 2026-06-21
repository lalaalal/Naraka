package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ResourceReloadListenerRegistry;
import net.fabricmc.fabric.api.resource.v1.ResourceLoader;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.resources.PreparableReloadListener;

import java.util.function.Supplier;

public final class FabricResourceReloadListenerRegistry implements ResourceReloadListenerRegistry.Registrar {
    @Override
    public void register(Identifier location, Supplier<PreparableReloadListener> listener) {
        ResourceLoader.get(PackType.CLIENT_RESOURCES)
                .registerReloadListener(location, listener.get());
    }
}
