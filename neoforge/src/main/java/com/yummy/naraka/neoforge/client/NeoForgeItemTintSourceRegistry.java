package com.yummy.naraka.neoforge.client;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.init.ItemTintSourceRegistry;
import com.yummy.naraka.neoforge.NarakaEventBus;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterColorHandlersEvent;

public final class NeoForgeItemTintSourceRegistry implements ItemTintSourceRegistry.Registrar, NarakaEventBus {
    @Override
    public void register(Identifier identifier, MapCodec<? extends ItemTintSource> mapCodec) {
        NARAKA_BUS.addListener(RegisterColorHandlersEvent.ItemTintSources.class, event -> {
            event.register(identifier, mapCodec);
        });
    }
}
