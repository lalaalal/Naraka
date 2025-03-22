package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.model.HerobrineScarfModel;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.Herobrine;
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

public class HerobrineScarfLayer extends RenderLayer<Herobrine, HerobrineModel<Herobrine>> {
    private final HerobrineScarfModel scarfModel;

    public HerobrineScarfLayer(RenderLayerParent<Herobrine, HerobrineModel<Herobrine>> renderer, EntityRendererProvider.Context context) {
        super(renderer);
        this.scarfModel = new HerobrineScarfModel(context.bakeLayer(NarakaModelLayers.HEROBRINE_SCARF));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Herobrine herobrine, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        if (herobrine.getPhase() != 2)
            return;
        poseStack.pushPose();

        RenderType renderType = RenderType.entitySmoothCutout(getTextureLocation(herobrine));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        scarfModel.copyModelFrom(getParentModel());
        scarfModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);

        RenderType waveRenderType = RenderType.entityCutout(getTextureLocation(herobrine));
        vertexConsumer = bufferSource.getBuffer(waveRenderType);
        poseStack.translate(0.25, 0.125, 0.25);
        poseStack.scale(-3, 3, 3);

        float rotationDegree = herobrine.getScarfRotationDegree(partialTick) - NarakaMod.config().herobrineScarfDefaultRotation.getValue();
        List<Float> speedList = herobrine.getScarfWaveSpeedList();
        renderScarf(poseStack, vertexConsumer, packedLight, ageInTicks, rotationDegree, 9, 27, 7, 15, 64, 64, 0, speedList);

        poseStack.popPose();
    }

    /**
     * I think y, z axis is flipped...
     *
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
        float degree = NarakaMod.config().herobrineScarfWaveMaxAngle.getValue();
        float speed = NarakaMod.config().herobrineScarfWaveSpeed.getValue();
        float waveCycleModifier = NarakaMod.config().herobrineScarfWaveCycleModifier.getValue();

        float u = textureX / (float) textureWidth;
        float v = textureY / (float) textureHeight;
        float widthInRatio = width / (float) textureWidth;
        float heightInRatio = height / (float) textureHeight;
        int divisionValue = NarakaMod.config().herobrineScarfPartitionNumber.getValue();

        float partWidth = widthInRatio / divisionValue;
        float partHeight = heightInRatio / divisionValue;

        for (int i = 0; i < divisionValue; i++) {
            poseStack.pushPose();
            float theta = (ageInTicks - i * waveCycleModifier) * speed;
            float sex = theta * verticalSpeed.get(i);
            float offset = theta - theta / verticalSpeed.get(i);
            float xRot = (Mth.cos(sex - offset) + 1) * (float) Math.toRadians(degree) / 2 * verticalSpeed.get(i);
            poseStack.translate(offsetX, xRot_offsetY, xRot_offsetZ);

            float part_yOffset = Mth.sin(xRot) * partHeight;
            float zRot_OffsetY = 0;
            float zRot_OffsetX = 0;
            for (int j = 0; j < divisionValue; j++) {
                poseStack.pushPose();

                float zRot = (Mth.cos((ageInTicks - j * waveCycleModifier * horizontalSpeed) * speed) + 1) * (float) Math.toRadians(degree) / 4;
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

                float currentU = u + partWidth * j;
                float currentV = v + partHeight * i;
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
     * Add 4 vertices in anti-clockwise from left-top
     *
     * @param positions Positions, size must be 4
     */
    private static void vertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, List<Vector3f> positions, float u, float v, float width, float height, int packedLight, int packedOverlay, int color, Direction direction) {
        Vec3i normal = direction.getNormal();
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

    @Override
    protected ResourceLocation getTextureLocation(Herobrine entity) {
        return NarakaTextures.HEROBRINE_SCARF;
    }
}
