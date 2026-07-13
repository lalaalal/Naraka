package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.ShinyEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
public class ShinyEffectRenderer extends EntityRenderer<ShinyEffect> {
    public ShinyEffectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(ShinyEffect livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(ShinyEffect entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float ageInTicks = entity.tickCount + partialTick;

        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getRotation()));
        renderShiny(ageInTicks, entity.getLifetime(), entity.getScale(), entity.isVertical(), entity.getColor(), poseStack, bufferSource);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ShinyEffect entity) {
        return NarakaMod.location("empty");
    }


    public static void renderShiny(float tick, int lifetime, float scale, boolean isVertical, int color, PoseStack poseStack, MultiBufferSource bufferSource) {
        if (tick < 0 || tick > lifetime)
            return;

        Minecraft minecraft = Minecraft.getInstance();
        Entity camera = minecraft.getCameraEntity();
        if (camera != null)
            renderShiny(tick, lifetime, scale, camera.getYRot() + 180, isVertical, color, poseStack, bufferSource);
    }

    public static void renderShiny(float tick, int lifetime, float scale, float yRot, boolean isVertical, int color, PoseStack poseStack, MultiBufferSource bufferSource) {
        if (tick < 0 || tick > lifetime)
            return;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);

        poseStack.mulPose(Axis.YN.rotationDegrees(yRot));
        if (isVertical)
            poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(NarakaRenderTypes.emissive());
        renderShiny(poseStack, vertexConsumer, tick, lifetime, color);

        poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        renderShiny(poseStack, vertexConsumer, tick, lifetime, color);
        poseStack.popPose();
    }

    private static void renderShiny(PoseStack poseStack, VertexConsumer vertexConsumer, float tick, int lifetime, int color) {
        poseStack.pushPose();
        float width = NarakaUtils.interpolate(tick / lifetime, 0, 20, NarakaUtils::fastStepIn);
        float height = NarakaUtils.interpolate(tick / lifetime, 0.1f, 0, NarakaUtils::fastStepOut);

        NarakaRenderUtils.renderRhombus(poseStack.last(), vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, width, height, 0xff, 0xffffff);

        float centerWidth = Math.min(0.5f, width);
        float centerHeight = NarakaUtils.interpolate(tick / lifetime, 0.5f, 0, NarakaUtils::fastStepOut);
        int alpha = 0xff;
        float alphaMultiplier = 0.3f;
        if (color == 0xffffff)
            alphaMultiplier = 0.25f;
        while (centerWidth < width) {
            poseStack.translate(0, 0, 0.01);
            NarakaRenderUtils.renderRhombus(poseStack.last(), vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, centerWidth, centerHeight, alpha, color);
            centerWidth *= 2;
            alpha = (int) (alpha * alphaMultiplier);
            centerHeight += height * 0.5f;
            alphaMultiplier = Math.min(1, alphaMultiplier + (1 - alphaMultiplier) * 0.7f);
        }
        float previousCenterWidth = centerWidth / 2;
        float delta = (width - previousCenterWidth) / (centerWidth - previousCenterWidth);
        int lastAlpha = Mth.lerpInt(delta, 0, alpha);
        NarakaRenderUtils.renderRhombus(poseStack.last(), vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, width, centerHeight, lastAlpha, color);
        poseStack.popPose();
    }
}
