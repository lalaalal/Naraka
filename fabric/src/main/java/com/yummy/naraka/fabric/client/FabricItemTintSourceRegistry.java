package com.yummy.naraka.fabric.client;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.init.ItemTintSourceRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.client.color.item.ItemTintSources;
import net.minecraft.resources.Identifier;

public final class FabricItemTintSourceRegistry {
    @MethodProxy(ItemTintSourceRegistry.class)
    public static void register(Identifier identifier, MapCodec<? extends ItemTintSource> mapCodec) {
        ItemTintSources.ID_MAPPER.put(identifier, mapCodec);
    }
}
