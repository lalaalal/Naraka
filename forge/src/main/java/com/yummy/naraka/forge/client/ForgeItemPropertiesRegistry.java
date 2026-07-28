package com.yummy.naraka.forge.client;

import com.yummy.naraka.client.init.ItemPropertyRegistry;
import net.minecraft.client.renderer.item.ClampedItemPropertyFunction;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public final class ForgeItemPropertiesRegistry implements ItemPropertyRegistry.Registrar {
    @Override
    public void register(ItemLike item, ResourceLocation id, ClampedItemPropertyFunction function) {
        ItemProperties.register(item.asItem(), id, function);
    }
}
