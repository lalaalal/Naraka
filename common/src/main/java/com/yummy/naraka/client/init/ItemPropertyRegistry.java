package com.yummy.naraka.client.init;

import com.yummy.naraka.client.service.NarakaClientServices;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

public abstract class ItemPropertyRegistry {
    public static void register(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        NarakaClientServices.ITEM_PROPERTY_REGISTRY.register(item, id, function);
    }

    public interface Registrar {
        void register(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function);
    }
}
