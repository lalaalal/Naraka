package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.HerobrineModel;
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

public class HerobrineScarfLayer extends RenderLayer<Herobrine, HerobrineModel<Herobrine>> {
    public HerobrineScarfLayer(RenderLayerParent<Herobrine, HerobrineModel<Herobrine>> renderer, EntityRendererProvider.Context context) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, Herobrine herobrine, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        poseStack.translate(0, 0, 0.25);

        RenderType renderType = RenderType.entityCutout(getTextureLocation(herobrine));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        renderScarf(poseStack, vertexConsumer, packedLight, ageInTicks, 70, 0, 0, 8, 16, 16, 16);

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
    public void renderScarf(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, float ageInTicks, float rotationDegree, int textureX, int textureY, int width, int height, int textureWidth, int textureHeight) {
        poseStack.pushPose();
        poseStack.rotateAround(Axis.XN.rotationDegrees(rotationDegree), 0, 0, 0);

        float offsetY = 0;
        float offsetZ = 0;
        float degree = NarakaMod.config().herobrineScarfWaveMaxAngle.getValue();
        float speed = NarakaMod.config().herobrineScarfWaveSpeed.getValue();
        float waveCycleModifier = NarakaMod.config().herobrineScarfWaveCycleModifier.getValue();

        float u = textureX / (float) textureWidth;
        float v = textureY / (float) textureHeight;
        float widthInRatio = width / (float) textureWidth;
        float heightInRatio = height / (float) textureHeight;
        int divisionValue = 16;

        float halfWidth = widthInRatio / 2;
        float partHeight = heightInRatio / divisionValue;

        Vector3f[] down = new Vector3f[]{
                new Vector3f(-halfWidth, 0, partHeight),
                new Vector3f(-halfWidth, 0, 0),
                new Vector3f(halfWidth, 0, 0),
                new Vector3f(halfWidth, 0, partHeight)
        };
        Vector3f[] up = new Vector3f[]{
                new Vector3f(-halfWidth, 0, partHeight),
                new Vector3f(halfWidth, 0, partHeight),
                new Vector3f(halfWidth, 0, 0),
                new Vector3f(-halfWidth, 0, 0)
        };

        for (int i = 0; i < divisionValue; i++) {
            poseStack.pushPose();
            float rot = (Mth.cos((ageInTicks - i * waveCycleModifier) * speed) + 1) * (float) Math.toRadians(degree) / 2;
            poseStack.translate(0, offsetY, offsetZ);
            poseStack.rotateAround(Axis.XP.rotation(rot), 0, 0, 0);
            float currentU = u + widthInRatio * i;
            float currentV = v + partHeight * i;
            vertices(vertexConsumer, poseStack.last(), down, currentU, currentV, widthInRatio, partHeight, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.DOWN);
            vertices(vertexConsumer, poseStack.last(), up, currentU, currentV, widthInRatio, partHeight, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.UP);

            offsetY -= Mth.sin(rot) * partHeight;
            offsetZ += Mth.cos(rot) * partHeight;
            poseStack.popPose();
        }
        poseStack.popPose();
    }

    /**
     * Add 4 vertices in anti-clockwise from left-top
     *
     * @param positions Positions, size must be 4
     */
    private static void vertices(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vector3f[] positions, float u, float v, float width, float height, int packedLight, int packedOverlay, int color, Direction direction) {
        Vec3i normal = direction.getNormal();
        Vector2f[] uvs = new Vector2f[]{
                new Vector2f(u, v),
                new Vector2f(u, v + height),
                new Vector2f(u + width, v + height),
                new Vector2f(u + width, v)
        };
        NarakaUtils.iterate(positions, uvs, (position, uv) -> {
            vertexConsumer.addVertex(pose, position)
                    .setColor(color)
                    .setLight(packedLight)
                    .setOverlay(packedOverlay)
                    .setUv(uv.x, uv.y)
                    .setNormal(pose, normal.getX(), normal.getY(), normal.getZ());
        });
    }

    @Override
    protected ResourceLocation getTextureLocation(Herobrine entity) {
        return NarakaTextures.HEROBRINE_SCARF;
    }
}
