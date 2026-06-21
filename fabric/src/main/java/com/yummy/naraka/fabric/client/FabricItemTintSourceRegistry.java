package com.yummy.naraka.fabric.client;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.init.ItemTintSourceRegistry;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.Identifier;

public final class FabricItemTintSourceRegistry implements ItemTintSourceRegistry.Registrar {
    @Override
    public void register(Identifier identifier, MapCodec<? extends ItemTintSource> mapCodec) {
        ItemTintSources.ID_MAPPER.put(identifier, mapCodec);
    }
}
