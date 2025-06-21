package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.HerobrineScarfModel;
import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfPose;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfTexture;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.ScarfWavingData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class HerobrineScarfLayer<S extends AbstractHerobrineRenderState, M extends AbstractHerobrineModel<S>> extends RenderLayer<S, M> {
    private final HerobrineScarfModel scarfModel;

    public HerobrineScarfLayer(RenderLayerParent<S, M> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.scarfModel = new HerobrineScarfModel(context.bakeLayer(NarakaModelLayers.HEROBRINE_SCARF));
    }

    private static <S extends AbstractHerobrineRenderState, M extends AbstractHerobrineModel<S>> void applyTranslateAndRotate(PoseStack poseStack, M herobrineModel) {
        herobrineModel.root().translateAndRotate(poseStack);
        herobrineModel.main().translateAndRotate(poseStack);
        herobrineModel.upperBody().translateAndRotate(poseStack);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, S renderState, float yRot, float xRot) {
        if (!renderState.renderScarf)
            return;
        poseStack.pushPose();
        applyTranslateAndRotate(poseStack, getParentModel());
        int color = selectColor(renderState);
        if (renderState.getModelType() != WavingScarfRenderState.ModelType.BIG) {
            RenderType renderType = RenderType.entitySmoothCutout(renderState.getFixedModelTexture());
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

            scarfModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
        }

        for (WavingScarfRenderState.ModelData modelData : renderState.scarfRenderState.modelDataList) {
            poseStack.pushPose();
            WavingScarfPose scarfPose = modelData.pose();
            WavingScarfTexture textureInfo = modelData.textureInfo();
            RenderType waveRenderType = RenderType.entityTranslucent(textureInfo.texture(renderState.isShadow));
            VertexConsumer vertexConsumer = bufferSource.getBuffer(waveRenderType);
            float scale = scarfPose.scale();
            Vec3 translation = scarfPose.translation();
            poseStack.scale(-scale, scale, scale);
            poseStack.translate(translation);

            renderScarf(poseStack, vertexConsumer, packedLight, color, renderState.scarfRenderState, modelData);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private int selectColor(S renderState) {
        if (renderState.isShadow)
            return NarakaConfig.CLIENT.shadowHerobrineColor.getValue().withAlpha(renderState.scarfAlpha).pack();
        return -1;
    }

    public void renderScarf(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int color, WavingScarfRenderState renderState, WavingScarfRenderState.ModelData modelData) {
        WavingScarfTexture textureInfo = modelData.textureInfo();
        WavingScarfPose scarfPose = modelData.pose();
        ScarfWavingData waveData = renderState.waveData;

        poseStack.pushPose();
        poseStack.rotateAround(Axis.XP.rotationDegrees(renderState.rotationDegree), 0, 0, 0);

        float partialTick = renderState.partialTick;

        int verticalSize = waveData.getVerticalSize();
        int horizontalSize = waveData.getHorizontalSize();

        float partWidth = textureInfo.widthInRatio() / horizontalSize;
        float partHeight = textureInfo.heightInRatio() / verticalSize;

        float u = textureInfo.u();
        float v = textureInfo.v();

        float baseY = waveData.getVerticalPosition(scarfPose.waveOffset() - 1, partialTick) + waveData.getHorizontalPosition(-1, partialTick);
        float shift = 0;
        for (int vertical = 0; vertical < verticalSize; vertical++) {
            int verticalIndex = vertical + scarfPose.waveOffset();
            poseStack.pushPose();
            float partShift = waveData.getVerticalShift(vertical, partialTick);
            shift += partShift;
            poseStack.translate(shift, -baseY, partHeight * vertical);

            for (int horizontal = 0; horizontal < horizontalSize; horizontal++) {
                poseStack.pushPose();
                poseStack.translate(partWidth * horizontal, 0, 0);

                float topLeft_y = waveData.getVerticalPosition(verticalIndex - 1, partialTick) + waveData.getHorizontalPosition(horizontal - 1, partialTick);
                float topRight_y = waveData.getVerticalPosition(verticalIndex - 1, partialTick) + waveData.getHorizontalPosition(horizontal, partialTick);
                float bottomLeft_y = waveData.getVerticalPosition(verticalIndex, partialTick) + waveData.getHorizontalPosition(horizontal - 1, partialTick);
                float bottomRight_y = waveData.getVerticalPosition(verticalIndex, partialTick) + waveData.getHorizontalPosition(horizontal, partialTick);

                List<Vector3f> vertices = List.of(
                        new Vector3f(partShift, bottomLeft_y, partHeight),
                        new Vector3f(0, topLeft_y, 0),
                        new Vector3f(partWidth, topRight_y, 0),
                        new Vector3f(partWidth + partShift, bottomRight_y, partHeight)
                );

                float currentU = u + partWidth * horizontal;
                float currentV = v + partHeight * vertical;

                NarakaRenderUtils.vertices(vertexConsumer, poseStack.last(), vertices, currentU, currentV, partWidth, partHeight, packedLight, OverlayTexture.NO_OVERLAY, color, Direction.UP);
                NarakaRenderUtils.vertices(vertexConsumer, poseStack.last(), vertices, currentU, currentV, partWidth, partHeight, packedLight, OverlayTexture.NO_OVERLAY, color, Direction.DOWN);
                poseStack.popPose();
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
