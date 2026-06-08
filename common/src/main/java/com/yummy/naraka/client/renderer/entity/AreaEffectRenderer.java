package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AreaEffect;
import com.yummy.naraka.world.entity.AreaShape;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class AreaEffectRenderer extends EntityRenderer<AreaEffect> {
    public AreaEffectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(AreaEffect livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    private RenderType getRenderType(AreaEffect entity) {
        if (entity.getMaxAlpha() == 0xff) {
            if (NarakaClientContext.SHADER_ENABLED.getValue())
                return RenderType.entityTranslucent(NarakaTextures.AREA_EFFECT);
            return RenderType.debugQuads();
        }
        return RenderType.lightning();
    }

    @Override
    public void render(AreaEffect entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float ageInTicks = entity.tickCount + partialTick;

        int color = entity.getColor();
        int alpha = (int) NarakaUtils.interpolate(ageInTicks / entity.getLifetime(), 0, entity.getMaxAlpha(), NarakaUtils::fadeInOut);
        AreaShape shape = entity.getShape();
        poseStack.pushPose();

        float x = entity.getXWidth() / 2;
        float z = entity.getZWidth() / 2;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(getRenderType(entity));

        PoseStack.Pose pose = poseStack.last();
        if (shape == AreaShape.RECTANGLE)
            renderRectangle(pose, vertexConsumer, entity.getXWidth(), entity.getZWidth(), alpha, color);
        if (shape == AreaShape.CIRCLE)
            renderCircle(pose, vertexConsumer, new Vector3f(0, 0.1f, 0), entity.getXWidth() / 2, alpha, color);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    public static void renderRectangle(PoseStack.Pose pose, VertexConsumer vertexConsumer, float xWidth, float zWidth, int alpha, int color) {
        float x = xWidth / 2;
        float z = zWidth / 2;
        vertexConsumer.addVertex(pose, x, 0.1f, z)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, color))
                .setNormal(pose, 0, 1, 0);
        vertexConsumer.addVertex(pose, x, 0.1f, -z)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, color))
                .setNormal(pose, 0, 1, 0);
        vertexConsumer.addVertex(pose, -x, 0.1f, -z)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, color))
                .setNormal(pose, 0, 1, 0);
        vertexConsumer.addVertex(pose, -x, 0.1f, z)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, color))
                .setNormal(pose, 0, 1, 0);
    }

    public static void renderCircle(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3f base, float radius, int alpha, int color) {
        float prevTheta = 0;
        float interval = (0.1f / radius);
        for (float theta = interval; prevTheta < Math.PI * 2; theta += interval) {
            float x1 = Mth.cos(prevTheta) * radius + base.x();
            float z1 = Mth.sin(prevTheta) * radius + base.z();

            float currentTheta = Math.min(theta, Mth.PI * 2);
            float x2 = Mth.cos(currentTheta) * radius + base.x();
            float z2 = Mth.sin(currentTheta) * radius + base.z();

            Vector3f v1 = new Vector3f(x1, base.y(), z1);
            Vector3f v2 = new Vector3f(x2, base.y(), z2);

            addVertex(pose, vertexConsumer, v1, 0, 1, alpha, color);
            addVertex(pose, vertexConsumer, base, 0, 0, alpha, color);
            addVertex(pose, vertexConsumer, v2, 1, 0, alpha, color);
            addVertex(pose, vertexConsumer, v1, 1, 1, alpha, color);

            prevTheta = theta;
        }
    }

    private static void addVertex(PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3f vector, float u, float v, int alpha, int color) {
        vertexConsumer.addVertex(pose, vector)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, color))
                .setNormal(pose, 0, 1, 0);
    }

    @Override
    public ResourceLocation getTextureLocation(AreaEffect entity) {
        return NarakaTextures.AREA_EFFECT;
    }
}
