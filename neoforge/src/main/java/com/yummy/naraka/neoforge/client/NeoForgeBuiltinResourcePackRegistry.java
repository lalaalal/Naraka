package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.BuiltinResourcePackRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public final class NeoForgeBuiltinResourcePackRegistry implements NarakaEventBus {
    @MethodProxy(BuiltinResourcePackRegistry.class)
    public static void register(ResourceLocation resourcePackId, Component displayName) {
        ResourceLocation patchedResourcePackId = resourcePackId.withPath("resourcepacks/" + resourcePackId.getPath());
        NARAKA_BUS.addListener(AddPackFindersEvent.class, event -> {
            event.addPackFinders(patchedResourcePackId, PackType.CLIENT_RESOURCES, displayName, PackSource.BUILT_IN, false, Pack.Position.TOP);
        });
    }
}
