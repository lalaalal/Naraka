package com.yummy.naraka.neoforge.client;

import com.yummy.naraka.client.init.ItemPropertyRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@SuppressWarnings("unused")
@OnlyIn(Dist.CLIENT)
public final class NeoForgeItemPropertiesRegistry {
    @MethodProxy(ItemPropertyRegistry.class)
    public static void register(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        ItemProperties.register(item.asItem(), id, function);
    }
}
