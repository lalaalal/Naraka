package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.model.HerobrineScarfModel;
import com.yummy.naraka.client.renderer.entity.state.HerobrineRenderState;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.NarakaUtils;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class HerobrineScarfLayer extends RenderLayer<HerobrineRenderState, HerobrineModel<HerobrineRenderState>> {
    private final HerobrineScarfModel scarfModel;

    public HerobrineScarfLayer(RenderLayerParent<HerobrineRenderState, HerobrineModel<HerobrineRenderState>> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.scarfModel = new HerobrineScarfModel(context.bakeLayer(NarakaModelLayers.HEROBRINE_SCARF));
    }

    private static void applyTranslateAndRotate(PoseStack poseStack, HerobrineModel herobrineModel) {
        herobrineModel.body().translateAndRotate(poseStack);
        herobrineModel.upperBody().translateAndRotate(poseStack);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, HerobrineRenderState renderState, float yRot, float xRot) {
        if (!renderState.renderScarf)
            return;
        poseStack.pushPose();

        RenderType renderType = RenderType.entitySmoothCutout(getTextureLocation());
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        applyTranslateAndRotate(poseStack, getParentModel());
        scarfModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

        RenderType waveRenderType = RenderType.entityCutout(getTextureLocation());
        vertexConsumer = bufferSource.getBuffer(waveRenderType);
        poseStack.translate(0.25, -0.625, 0.375);
        poseStack.scale(-3, 3, 3);

        float rotationDegree = renderState.scarfRotationDegree;
        List<Float> speedList = renderState.scarfWaveSpeedList;
        renderScarf(poseStack, vertexConsumer, packedLight, renderState.ageInTicks, rotationDegree, 9, 27, 7, 15, 64, 64, 0, speedList);

        poseStack.popPose();
    }

    /**
     * @param poseStack      Pose stack
     * @param vertexConsumer Vertex consumer
     * @param packedLight    Light value
     * @param ageInTicks     Tick count with partial ticks
     * @param rotationDegree Rotation for scarf start point
     * @param textureX       Texture start horizontal position in pixel
     * @param textureY       Texture start vertical position in pixel
     * @param width          Wanted width from actual texture in pixel
     * @param height         Wanted height from actual texture in pixel
     * @param textureWidth   Entire texture width in pixel
     * @param textureHeight  Entire texture height in pixel
     */
    public void renderScarf(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, float ageInTicks, float rotationDegree, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight, float horizontalSpeed, List<Float> verticalSpeed) {
        poseStack.pushPose();
        poseStack.rotateAround(Axis.XP.rotationDegrees(rotationDegree), 0, 0, 0);

        float horizontalShift = horizontalSpeed * 0.01f;
        float offsetX = 0;
        float xRot_offsetY = 0;
        float xRot_offsetZ = 0;
        float degree = NarakaConfig.CLIENT.herobrineScarfWaveMaxAngle.getValue();
        float speed = NarakaConfig.CLIENT.herobrineScarfWaveSpeed.getValue();
        float waveCycleModifier = NarakaConfig.CLIENT.herobrineScarfWaveCycleModifier.getValue();

        float u = textureX / (float) textureWidth;
        float v = textureY / (float) textureHeight;
        float widthInRatio = width / (float) textureWidth;
        float heightInRatio = height / (float) textureHeight;
        int divisionValue = NarakaConfig.CLIENT.herobrineScarfPartitionNumber.getValue();
        if (verticalSpeed.size() < divisionValue)
            return;

        float partWidth = widthInRatio / divisionValue;
        float partHeight = heightInRatio / divisionValue;

        for (int vertical = 0; vertical < divisionValue; vertical++) {
            poseStack.pushPose();
            float theta = (ageInTicks - vertical * waveCycleModifier) * speed;
            float scaledTheta = theta * verticalSpeed.get(vertical);
            float offset = theta - theta / verticalSpeed.get(vertical);
            float xRot = (Mth.cos(scaledTheta - offset) + 1) * (float) Math.toRadians(degree) / 2 * verticalSpeed.get(vertical);
            poseStack.translate(offsetX, xRot_offsetY, xRot_offsetZ);

            float part_yOffset = Mth.sin(xRot) * partHeight;
            float zRot_OffsetY = 0;
            float zRot_OffsetX = 0;
            for (int horizontal = 0; horizontal < divisionValue; horizontal++) {
                poseStack.pushPose();

                float zRot = (Mth.cos((ageInTicks - horizontal * waveCycleModifier * horizontalSpeed) * speed) + 1) * (float) Math.toRadians(degree) / 4;
                poseStack.translate(zRot_OffsetX, zRot_OffsetY, 0);

                float zRot_yOffset = Mth.sin(zRot) * partWidth;

                zRot_OffsetY += zRot_yOffset;
                zRot_OffsetX += Mth.cos(zRot) * partWidth;

                List<Vector3f> vertices = List.of(
                        new Vector3f(horizontalShift, part_yOffset, partHeight),
                        new Vector3f(0, 0, 0),
                        new Vector3f(partWidth, zRot_yOffset, 0),
                        new Vector3f(partWidth + horizontalShift, part_yOffset + zRot_yOffset, partHeight)
                );

                float currentU = u + partWidth * horizontal;
                float currentV = v + partHeight * vertical;
                vertices(vertexConsumer, poseStack.last(), vertices, currentU, currentV, partWidth, partHeight, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.UP);
                vertices(vertexConsumer, poseStack.last(), vertices, currentU, currentV, partWidth, partHeight, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.DOWN);

                poseStack.popPose();
            }

            offsetX += horizontalShift;
            xRot_offsetY += Mth.sin(xRot) * partHeight;
            xRot_offsetZ += Mth.cos(xRot) * partHeight;

            poseStack.popPose();
        }
        poseStack.popPose();
    }

    /**
     * Add 4 vertices in anti-clockwise from left-top based on a positive direction
     *
     * @param positions Positions, size must be 4
     */
    private static void vertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, List<Vector3f> positions, float u, float v, float width, float height, int packedLight, int packedOverlay, int color, Direction direction) {
        Vec3i normal = direction.getUnitVec3i();
        List<Vector2f> uvs = List.of(
                new Vector2f(u, v + height),
                new Vector2f(u, v),
                new Vector2f(u + width, v),
                new Vector2f(u + width, v + height)
        );
        NarakaUtils.iterate(positions, uvs, (position, uv) -> {
            vertexConsumer.addVertex(pose, position)
                    .setColor(color)
                    .setLight(packedLight)
                    .setOverlay(packedOverlay)
                    .setUv(uv.x, uv.y)
                    .setNormal(pose, normal.getX(), normal.getY(), normal.getZ());
        }, normal.getX() < 0 || normal.getY() < 0 || normal.getZ() < 0);
    }

    protected ResourceLocation getTextureLocation() {
        return NarakaTextures.HEROBRINE_SCARF;
    }
}
