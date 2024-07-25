package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.model.CustomItemRenderManager;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
@Environment(EnvType.CLIENT)
public class ItemRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void render(ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, BakedModel bakedModel, CallbackInfo ci) {
        if (CustomItemRenderManager.isItemRegistered(itemStack)) {
            CustomItemRenderManager.CustomItemRenderer itemRenderer = CustomItemRenderManager.getCustomRenderer(itemStack);
            if (itemRenderer.shouldRenderCustom(itemStack, itemDisplayContext)) {
                itemRenderer.render(itemStack, itemDisplayContext, poseStack, multiBufferSource, light, overlay);
                ci.cancel();
            }
        }
    }
}
