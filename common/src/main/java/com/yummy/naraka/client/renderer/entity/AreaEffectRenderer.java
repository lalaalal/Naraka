package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AreaEffect;
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

        int alpha = (int) NarakaUtils.interpolate(ageInTicks / entity.getLifetime(), 0, entity.getMaxAlpha(), this::fadeInOut);
        poseStack.pushPose();

        float x = entity.getXWidth() / 2;
        float z = entity.getZWidth() / 2;
        VertexConsumer vertexConsumer = bufferSource.getBuffer(getRenderType(entity));

        PoseStack.Pose pose = poseStack.last();
        vertexConsumer.addVertex(pose, x, 0.1f, z)
                .setUv(0, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, entity.getColor()))
                .setNormal(pose, 0, 1, 0);
        vertexConsumer.addVertex(pose, x, 0.1f, -z)
                .setUv(0, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, entity.getColor()))
                .setNormal(pose, 0, 1, 0);
        vertexConsumer.addVertex(pose, -x, 0.1f, -z)
                .setUv(1, 0)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, entity.getColor()))
                .setNormal(pose, 0, 1, 0);
        vertexConsumer.addVertex(pose, -x, 0.1f, z)
                .setUv(1, 1)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setColor(FastColor.ARGB32.color(alpha, entity.getColor()))
                .setNormal(pose, 0, 1, 0);

        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(AreaEffect entity) {
        return NarakaTextures.AREA_EFFECT;
    }

    private float fadeInOut(float x) {
        if (x < 0.125f)
            return Mth.sin(4 * Mth.PI * x);
        if (x > 0.875f)
            return -Mth.sin(4 * Mth.PI * x);
        return 1;
    }
}
