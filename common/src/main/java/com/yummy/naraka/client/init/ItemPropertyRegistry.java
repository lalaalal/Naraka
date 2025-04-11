package com.yummy.naraka.client.init;

import com.yummy.naraka.invoker.MethodInvoker;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;

@Environment(EnvType.CLIENT)
public abstract class ItemPropertyRegistry {
    public static void register(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        MethodInvoker.invoke(ItemPropertyRegistry.class, "register", item, id, function);
    }
}
