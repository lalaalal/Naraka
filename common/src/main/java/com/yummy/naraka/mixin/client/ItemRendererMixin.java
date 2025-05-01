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
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.item.ItemStackRenderState;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.annotation.Nullable;
import java.util.List;

@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
    @Shadow @Final private ItemStackRenderState scratchItemStackRenderState;

    @Shadow @Final private ItemModelResolver resolver;

    @Unique
    @Nullable
    private static ItemStack naraka$currentItem;

    // TODO : Refactor custom item render
    @Inject(method = "renderStatic(Lnet/minecraft/world/entity/LivingEntity;Lnet/minecraft/world/item/ItemStack;Lnet/minecraft/world/item/ItemDisplayContext;ZLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource;Lnet/minecraft/world/level/Level;III)V", at = @At("HEAD"), cancellable = true)
    public void renderCustom(LivingEntity entity, ItemStack itemStack, ItemDisplayContext displayContext, boolean leftHand, PoseStack poseStack, MultiBufferSource bufferSource, Level level, int combinedLight, int combinedOverlay, int seed, CallbackInfo ci) {
        naraka$currentItem = itemStack;
        if (CustomRenderManager.hasCustomRenderer(itemStack)) {
            CustomRenderManager.CustomItemRenderer itemRenderer = CustomRenderManager.getCustomRenderer(itemStack);
            if (itemRenderer.shouldRenderCustom(itemStack, displayContext)) {
                this.resolver.updateForTopItem(this.scratchItemStackRenderState, itemStack, displayContext, leftHand, level, entity, seed);
                poseStack.pushPose();
                if (itemRenderer.applyTransform()) {
                    scratchItemStackRenderState.transform().apply(leftHand, poseStack);
                    poseStack.translate(-0.5F, -0.5F, -0.5F);
                }
                itemRenderer.render(itemStack, displayContext, poseStack, bufferSource, combinedLight, combinedOverlay);
                poseStack.popPose();
                ci.cancel();
            }
        }
    }

    @Inject(method = "renderModelLists", at = @At("HEAD"), cancellable = true)
    private static void renderCustom(BakedModel model, int[] tintLayers, int packedLight, int packedOverlay, PoseStack poseStack, VertexConsumer buffer, CallbackInfo ci) {
        if (naraka$currentItem == null)
            return;
        if (CustomRenderManager.shouldRenderRainbow(naraka$currentItem)) {
            naraka$renderRainbowModelLists(model, packedLight, packedOverlay, poseStack, buffer);
            ci.cancel();
        }

        if (CustomRenderManager.shouldRenderColored(naraka$currentItem)) {
            Color color = CustomRenderManager.getItemColor(naraka$currentItem);
            naraka$renderColoredModelLists(model, packedLight, packedOverlay, poseStack, buffer, color);
            ci.cancel();
        }
    }

    @Unique
    private static void naraka$renderRainbowModelLists(BakedModel model, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer) {
        Color color = ComponentStyles.LONGINUS_COLOR.getCurrentColor().withAlpha(0xff);
        naraka$renderModelLists(model, combinedLight, combinedOverlay, poseStack, buffer, color);
    }

    @Unique
    private static void naraka$renderColoredModelLists(BakedModel model, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer, Color color) {
        naraka$renderModelLists(model, combinedLight, combinedOverlay, poseStack, buffer, color);
    }

    @Unique
    private static void naraka$renderModelLists(BakedModel model, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer, Color color) {
        RandomSource randomSource = RandomSource.create();

        for (Direction direction : Direction.values()) {
            randomSource.setSeed(42L);
            naraka$renderColoredQuadList(poseStack, buffer, model.getQuads(null, direction, randomSource), combinedLight, combinedOverlay, color);
        }

        randomSource.setSeed(42L);
        naraka$renderColoredQuadList(poseStack, buffer, model.getQuads(null, null, randomSource), combinedLight, combinedOverlay, color);
    }

    @Unique
    private static void naraka$renderColoredQuadList(PoseStack poseStack, VertexConsumer buffer, List<BakedQuad> quads, int combinedLight, int combinedOverlay, Color color) {
        PoseStack.Pose pose = poseStack.last();

        for (BakedQuad bakedQuad : quads)
            buffer.putBulkData(pose, bakedQuad, color.red01(), color.green01(), color.blue01(), color.alpha01(), combinedLight, combinedOverlay);
    }
}
