package com.yummy.naraka.mixin.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.CustomRenderManager;
import com.yummy.naraka.util.Color;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
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
    public void renderCustom(ItemStack itemStack, ItemDisplayContext itemDisplayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource multiBufferSource, int light, int overlay, BakedModel bakedModel, CallbackInfo ci) {
        if (CustomRenderManager.hasCustomRenderer(itemStack)) {
            CustomRenderManager.CustomItemRenderer itemRenderer = CustomRenderManager.getCustomRenderer(itemStack);
            if (itemRenderer.shouldRenderCustom(itemStack, itemDisplayContext)) {
                poseStack.pushPose();
                if (itemRenderer.applyTransform()) {
                    bakedModel.getTransforms().getTransform(itemDisplayContext).apply(leftHand, poseStack);
                    poseStack.translate(-0.5F, -0.5F, -0.5F);
                }
                itemRenderer.render(itemStack, itemDisplayContext, poseStack, multiBufferSource, light, overlay);
                poseStack.popPose();
                ci.cancel();
            }
        }
    }

    @ModifyExpressionValue(method = "render", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemBlockRenderTypes;getRenderType(Lnet/minecraft/world/item/ItemStack;Z)Lnet/minecraft/client/renderer/RenderType;"), require = 0)
    public RenderType modifyRenderType(RenderType original, @Local(argsOnly = true) ItemStack itemStack) {
        if (CustomRenderManager.hasCustomRenderType(itemStack))
            return CustomRenderManager.getCustomRenderType(itemStack, original);
        return original;
    }

    @Inject(method = "renderModelLists", at = @At("HEAD"), cancellable = true)
    public void renderCustom(BakedModel model, ItemStack itemStack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer, CallbackInfo ci) {
        if (CustomRenderManager.shouldRenderColored(itemStack)) {
            ci.cancel();
            naraka$renderColoredModelLists(model, itemStack, combinedLight, combinedOverlay, poseStack, buffer);
        }
    }

    @Unique
    private void naraka$renderColoredModelLists(BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer) {
        Color color = CustomRenderManager.getItemColor(stack);
        naraka$renderModelLists(model, stack, combinedLight, combinedOverlay, poseStack, buffer, color);
    }

    @Unique
    private void naraka$renderModelLists(BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer, Color color) {
        RandomSource randomSource = RandomSource.create();

        for (Direction direction : Direction.values()) {
            randomSource.setSeed(42L);
            this.naraka$renderColoredQuadList(poseStack, buffer, model.getQuads(null, direction, randomSource), stack, combinedLight, combinedOverlay, color);
        }

        randomSource.setSeed(42L);
        this.naraka$renderColoredQuadList(poseStack, buffer, model.getQuads(null, null, randomSource), stack, combinedLight, combinedOverlay, color);
    }

    @Unique
    private void naraka$renderColoredQuadList(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, ItemStack itemStack, int combinedLight, int combinedOverlay, Color color) {
        if (itemStack.isEmpty())
            return;
        PoseStack.Pose pose = poseStack.last();

        for (BakedQuad bakedQuad : quads)
            buffer.putBulkData(pose, bakedQuad, color.red01(), color.green01(), color.blue01(), color.alpha01(), combinedLight, combinedOverlay);
    }
}
