package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.HerobrineScarfModel;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfPose;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfRenderState;
import com.yummy.naraka.client.renderer.entity.state.WavingScarfTexture;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.AbstractHerobrine;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class HerobrineScarfLayer<T extends AbstractHerobrine, M extends AbstractHerobrineModel<T>> extends RenderLayer<T, M> {
    private final HerobrineScarfModel scarfModel;

    public HerobrineScarfLayer(RenderLayerParent<T, M> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.scarfModel = new HerobrineScarfModel(context.bakeLayer(NarakaModelLayers.HEROBRINE_SCARF));
    }

    private static <T extends AbstractHerobrine, M extends AbstractHerobrineModel<T>> void applyTranslateAndRotate(PoseStack poseStack, M herobrineModel) {
        herobrineModel.root().translateAndRotate(poseStack);
        herobrineModel.main().translateAndRotate(poseStack);
        herobrineModel.upperBody().translateAndRotate(poseStack);
    }

    private RenderType getOutsideRenderType(T entity, WavingScarfTexture textureInfo) {
        if (entity.isShadow)
            return RenderType.entityTranslucent(textureInfo.texture(true));
        return RenderType.entityCutout(textureInfo.texture(false));
    }

    private RenderType getInsideRenderType(T entity, WavingScarfTexture textureInfo) {
        if (!entity.isFinalModel() || NarakaClientContext.SHADER_ENABLED.getValue())
            return getOutsideRenderType(entity, textureInfo);
        if (entity.isShadow)
            return RenderType.entityTranslucent(textureInfo.texture(true));
        return NarakaRenderTypes.longinusCutout(textureInfo.texture(false));
    }

    private WavingScarfRenderState.ModelType selectModelType(T entity) {
        if (entity.isFinalModel())
            return WavingScarfRenderState.ModelType.BIG;
        return WavingScarfRenderState.ModelType.SMALL;
    }

    public ResourceLocation getFixedModelTexture(T entity) {
        if (entity.isShadow)
            return NarakaTextures.SHADOW_HEROBRINE_SCARF;
        return NarakaTextures.HEROBRINE_SCARF;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, T livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (!livingEntity.shouldRenderScarf())
            return;
        poseStack.pushPose();
        applyTranslateAndRotate(poseStack, getParentModel());

        WavingScarfRenderState.ModelType modelType = selectModelType(livingEntity);

        int color = selectColor(livingEntity);
        if (modelType != WavingScarfRenderState.ModelType.BIG) {
            RenderType renderType = RenderType.entitySmoothCutout(getFixedModelTexture(livingEntity));
            VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
            scarfModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
        }

        for (WavingScarfRenderState.ModelData modelData : modelType.modelData) {
            poseStack.pushPose();
            WavingScarfPose scarfPose = modelData.pose();
            WavingScarfTexture textureInfo = modelData.textureInfo();
            RenderType outsideRenderType = getOutsideRenderType(livingEntity, textureInfo);
            RenderType insideRenderType = getInsideRenderType(livingEntity, textureInfo);
            float scale = scarfPose.scale();
            float rotationDegree = livingEntity.getScarfRotationDegree(partialTick) - NarakaConfig.CLIENT.herobrineScarfDefaultRotation.getValue();
            ScarfWavingData waveData = livingEntity.getScarfWavingData();
            Vec3 translation = scarfPose.translation();
            poseStack.scale(-scale, -scale, scale);
            poseStack.translate(translation.x, translation.y, translation.z);

            renderScarf(poseStack, insideRenderType, outsideRenderType, bufferSource, packedLight, color, partialTick, rotationDegree, waveData, modelData);
            poseStack.popPose();
        }

        poseStack.popPose();
    }

    private int selectColor(T entity) {
        if (entity.isShadow)
            return NarakaConfig.CLIENT.shadowHerobrineColor.getValue().withAlpha(entity.getScarfAlpha()).pack();
        return -1;
    }

    public void renderScarf(PoseStack poseStack, RenderType insideRenderType, RenderType outsideRenderType, MultiBufferSource bufferSource, int packedLight, int color, float partialTick, float rotationDegree, ScarfWavingData waveData, WavingScarfRenderState.ModelData modelData) {
        WavingScarfTexture textureInfo = modelData.textureInfo();
        WavingScarfPose scarfPose = modelData.pose();

        poseStack.pushPose();
        poseStack.rotateAround(Axis.XN.rotationDegrees(rotationDegree), 0, 0, 0);

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

                List<Vector2f> uvs = NarakaRenderUtils.createUVList(currentU, currentV, partWidth, partHeight);

                VertexConsumer insideVertexConsumer = bufferSource.getBuffer(insideRenderType);
                NarakaRenderUtils.vertices(poseStack.last(), insideVertexConsumer, vertices, uvs, packedLight, OverlayTexture.NO_OVERLAY, color, Direction.UP, false);

                VertexConsumer outsideVertexConsumer = bufferSource.getBuffer(outsideRenderType);
                NarakaRenderUtils.vertices(poseStack.last(), outsideVertexConsumer, vertices, uvs, packedLight, OverlayTexture.NO_OVERLAY, color, Direction.UP, true);

                poseStack.popPose();
            }
            poseStack.popPose();
        }
        poseStack.popPose();
    }
}
