package com.yummy.naraka.fabric.client;

import com.yummy.naraka.client.init.ItemPropertyRegistry;
import com.yummy.naraka.invoker.MethodProxy;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

@SuppressWarnings("unused")
@Environment(EnvType.CLIENT)
public final class FabricItemPropertyRegistry {
    @MethodProxy(ItemPropertyRegistry.class)
    public static void register(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        ItemProperties.register(item.asItem(), id, function);
    }
}
