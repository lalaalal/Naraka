package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.BuiltinResourcePackRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.packs.PackType;
import net.minecraft.server.packs.repository.Pack;
import net.minecraft.server.packs.repository.PackSource;
import net.neoforged.neoforge.event.AddPackFindersEvent;

public final class NeoForgeBuiltinResourcePackRegistry implements BuiltinResourcePackRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(Identifier resourcePackId, Component displayName) {
        Identifier patchedResourcePackId = resourcePackId.withPath("resourcepacks/" + resourcePackId.getPath());
        NARAKA_BUS.addListener(AddPackFindersEvent.class, event -> {
            event.addPackFinders(patchedResourcePackId, PackType.CLIENT_RESOURCES, displayName, PackSource.BUILT_IN, false, Pack.Position.TOP);
        });
    }
}
