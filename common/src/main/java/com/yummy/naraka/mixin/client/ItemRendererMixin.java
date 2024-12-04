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
@Mixin(value = ItemRenderer.class, priority = 990)
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

    @Inject(method = "renderModelLists", at = @At("HEAD"), cancellable = true)
    public void renderRainbow(BakedModel model, ItemStack itemStack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer, CallbackInfo ci) {
        if (CustomRenderManager.shouldRenderRainbow(itemStack)) {
            ci.cancel();
            naraka$renderModelLists(model, itemStack, combinedLight, combinedOverlay, poseStack, buffer);
        }
    }

    @Unique
    private void naraka$renderModelLists(BakedModel model, ItemStack stack, int combinedLight, int combinedOverlay, PoseStack poseStack, VertexConsumer buffer) {
        RandomSource randomSource = RandomSource.create();

        for (Direction direction : Direction.values()) {
            randomSource.setSeed(42L);
            this.naraka$renderQuadList(poseStack, buffer, model.getQuads(null, direction, randomSource), stack, combinedLight, combinedOverlay);
        }

        randomSource.setSeed(42L);
        this.naraka$renderQuadList(poseStack, buffer, model.getQuads(null, null, randomSource), stack, combinedLight, combinedOverlay);
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
