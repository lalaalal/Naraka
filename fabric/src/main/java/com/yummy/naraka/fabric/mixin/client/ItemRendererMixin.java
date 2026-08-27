package com.yummy.naraka.fabric.mixin.client;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.client.renderer.VertexConsumerExtension;
import com.yummy.naraka.util.Color;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Redirect(method = "renderQuadList", at = @At(value = "INVOKE", target = "Lcom/mojang/blaze3d/vertex/VertexConsumer;putBulkData(Lcom/mojang/blaze3d/vertex/PoseStack$Pose;Lnet/minecraft/client/renderer/block/model/BakedQuad;FFFII)V"))
    private void redirectPutBulkData(VertexConsumer instance, PoseStack.Pose poseEntry, BakedQuad quad, float red, float green, float blue, int combinedLight, int combinedOverlay, @Local(argsOnly = true) ItemStack stack) {
        if (CustomRenderManager.shouldRenderColored(stack) && instance instanceof VertexConsumerExtension vertexConsumerExtension) {
            Color color = CustomRenderManager.getItemColor(stack);
            vertexConsumerExtension.naraka$putBulkData(poseEntry, quad, color.red01(), color.green01(), color.blue01(), color.alpha01(), combinedLight, combinedOverlay);
        } else {
            instance.putBulkData(poseEntry, quad, red, green, blue, combinedLight, combinedOverlay);
        }
    }
}
