package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.util.ComponentStyles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Inject(method = "render", at = @At("HEAD"), cancellable = true)
    public void renderCustom(ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean bl, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, BakedModel bakedModel, CallbackInfo ci) {
        if (CustomRenderManager.hasCustomRenderer(itemStack)) {
            CustomRenderManager.CustomItemRenderer itemRenderer = CustomRenderManager.getCustomRenderer(itemStack);
            if (itemRenderer.shouldRenderCustom(itemStack, itemDisplayContext)) {
                itemRenderer.render(itemStack, itemDisplayContext, poseStack, multiBufferSource, light, overlay);
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderQuadList", at = @At("HEAD"), cancellable = true)
    public void renderRainbow(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, ItemStack itemStack, int combinedLight, int combinedOverlay, CallbackInfo ci) {
        if (CustomRenderManager.shouldRenderRainbow(itemStack)) {
            naraka$renderQuadList(poseStack, buffer, quads, itemStack, combinedLight, combinedOverlay);
            ci.cancel();
        }
    }

    @Unique
    private void naraka$renderQuadList(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, ItemStack itemStack, int combinedLight, int combinedOverlay) {
        if (itemStack.isEmpty())
            return;
        PoseStack.Pose pose = poseStack.last();

        for (BakedQuad bakedQuad : quads) {
            Color color = ComponentStyles.LONGINUS_COLOR.getCurrentColor();
            buffer.putBulkData(pose, bakedQuad, color.red01(), color.green01(), color.blue01(), 1, combinedLight, combinedOverlay);
        }
    }
}
