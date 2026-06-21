package com.yummy.naraka.client.init;

import com.mojang.serialization.MapCodec;
import com.yummy.naraka.client.NarakaClientServices;
import net.minecraft.client.color.item.ItemTintSource;
import net.minecraft.resources.Identifier;

public abstract class ItemTintSourceRegistry {
    public static void register(Identifier identifier, MapCodec<? extends ItemTintSource> mapCodec) {
        NarakaClientServices.ITEM_TINT_SOURCE_REGISTRY.register(identifier, mapCodec);
    }

    public interface Registrar {
        void register(Identifier identifier, MapCodec<? extends ItemTintSource> mapCodec);
    }
}
