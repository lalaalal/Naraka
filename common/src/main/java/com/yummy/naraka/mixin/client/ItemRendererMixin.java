package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderCustom(ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay, BakedModel model, CallbackInfo ci) {
        if (CustomRenderManager.hasCustomRenderer(itemStack)) {
            CustomRenderManager.CustomItemRenderer itemRenderer = CustomRenderManager.getCustomRenderer(itemStack);
            if (itemRenderer.shouldRenderCustom(itemStack, displayContext)) {
                poseStack.pushPose();
                if (itemRenderer.applyTransform()) {
                    model.getTransforms().getTransform(displayContext).apply(leftHand, poseStack);
                    poseStack.translate(-0.5F, -0.5F, -0.5F);
                }
                itemRenderer.render(itemStack, displayContext, poseStack, buffer, combinedLight, combinedOverlay);
                poseStack.popPose();
                ci.cancel();
            }
        }
    }

    /**
     * Platform specific implementation for applying custom item color tint
     */
    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemBlockRenderTypes;getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;"), require = 0)
    public RenderType modifyRenderType(RenderType original, @Local(argsOnly = true) ItemStack itemStack) {
        if (CustomRenderManager.hasCustomRenderType(itemStack))
            return CustomRenderManager.getCustomRenderType(itemStack, original);
        return original;
    }
}
