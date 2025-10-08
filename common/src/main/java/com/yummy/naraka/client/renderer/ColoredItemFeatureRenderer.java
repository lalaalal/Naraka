package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.OutlineBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.util.ARGB;

import java.util.List;

@Environment(EnvType.CLIENT)
public class ColoredItemFeatureRenderer {
    private final PoseStack poseStack = new PoseStack();

    public void render(ColoredItemSubmitNodeCollection coloredItemSubmitNodeCollection, MultiBufferSource.BufferSource bufferSource, OutlineBufferSource outlineBufferSource) {
        coloredItemSubmitNodeCollection.naraka$getColoredItemSubmits().forEach((itemSubmit, color) -> {
            this.poseStack.pushPose();
            this.poseStack.last().set(itemSubmit.pose());
            MultiBufferSource buffer = bufferSource;
            if (itemSubmit.outlineColor() != 0) {
                outlineBufferSource.setColor(itemSubmit.outlineColor());
                buffer = outlineBufferSource;
            }
            renderColoredItem(
                    this.poseStack,
                    buffer,
                    itemSubmit.lightCoords(),
                    itemSubmit.overlayCoords(),
                    color,
                    itemSubmit.quads(),
                    itemSubmit.renderType()
            );
            this.poseStack.popPose();
        });
    }

    private void renderColoredItem(
            PoseStack poseStack,
            MultiBufferSource bufferSource,
            int packedLight,
            int packedOverlay,
            int color,
            List<BakedQuad> quads,
            RenderType renderType
    ) {
        VertexConsumer vertexConsumer = ItemRenderer.getFoilBuffer(bufferSource, renderType, true, false);
        PoseStack.Pose pose = poseStack.last();

        for (BakedQuad bakedQuad : quads)
            vertexConsumer.putBulkData(pose, bakedQuad, ARGB.redFloat(color), ARGB.greenFloat(color), ARGB.blueFloat(color), ARGB.alphaFloat(color), packedLight, packedOverlay);
    }
}
