package com.yummy.naraka.client.init;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.invoker.MethodInvoker;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.resources.Identifier;

public abstract class ItemTintSourceRegistry {
    public static void register(Identifier identifier, MapCodec<? extends ItemTintSource> mapCodec) {
        MethodInvoker.of(ItemTintSourceRegistry.class, "register")
                .withParameterTypes(Identifier.class, MapCodec.class)
                .invoke(identifier, mapCodec);
    }
}
