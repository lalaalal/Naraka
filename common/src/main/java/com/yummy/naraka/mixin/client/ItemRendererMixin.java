package com.yummy.naraka.mixin.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.util.ComponentStyles;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.List;

@Deprecated
@Environment(EnvType.CLIENT)
@Mixin(ItemRenderer.class)
public abstract class ItemRendererMixin {
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
