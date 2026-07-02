package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.renderer.entity.state.ShinyEffectRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.ShinyEffect;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;

public class ShinyEffectRenderer extends EntityRenderer<ShinyEffect, ShinyEffectRenderState> {
    public ShinyEffectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ShinyEffectRenderState createRenderState() {
        return new ShinyEffectRenderState();
    }

    @Override
    public void extractRenderState(ShinyEffect entity, ShinyEffectRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.isVertical = entity.isVertical();
        reusedState.scale = entity.getScale();
        reusedState.lifetime = entity.getLifetime();
        reusedState.color = entity.getColor();
        reusedState.rotation = entity.getRotation();
    }

    @Override
    public boolean shouldRender(ShinyEffect livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected boolean affectedByCulling(ShinyEffect display) {
        return false;
    }

    public static void submitShiny(float tick, int lifetime, float scale, boolean isVertical, int color, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        submitShiny(tick, lifetime, scale, cameraRenderState.yRot + 180, isVertical, color, poseStack, submitNodeCollector);
    }

    public static void submitShiny(float tick, int lifetime, float scale, float yRot, boolean isVertical, int color, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        if (tick < 0 || tick > lifetime)
            return;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YN.rotationDegrees(yRot));
        if (isVertical)
            poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        submitShiny(tick, lifetime, color, poseStack, submitNodeCollector);

        poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        submitShiny(tick, lifetime, color, poseStack, submitNodeCollector);
        poseStack.popPose();
    }

    private static void submitShiny(float tick, int lifetime, int color, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        poseStack.pushPose();
        float width = NarakaUtils.interpolate(tick / lifetime, 0, 20, NarakaUtils::fastStepIn);
        float height = NarakaUtils.interpolate(tick / lifetime, 0.1f, 0, NarakaUtils::fastStepOut);

        submitNodeCollector.order(0).submitCustomGeometry(poseStack, RenderTypes.lightning(), renderRhombus(width, height, 0xff, 0xffffff));

        float centerWidth = Math.min(0.5f, width);
        float centerHeight = NarakaUtils.interpolate(tick / lifetime, 0.5f, 0, NarakaUtils::fastStepOut);
        int alpha = 0xff;
        int index = 1;
        float alphaMultiplier = 0.4f;
        if (color == 0xffffff)
            alphaMultiplier = 0.25f;
        while (centerWidth < width) {
            poseStack.translate(0, 0, 0.01);
            submitNodeCollector.order(index).submitCustomGeometry(poseStack, RenderTypes.lightning(), renderRhombus(centerWidth, centerHeight, alpha, color));
            centerWidth *= 2;
            alpha = (int) (alpha * alphaMultiplier);
            centerHeight += height * 0.5f;
            alphaMultiplier = Math.min(1, alphaMultiplier + (1 - alphaMultiplier) * 0.8f);
            index += 1;
        }

        submitNodeCollector.order(index).submitCustomGeometry(poseStack, RenderTypes.lightning(), renderRhombus(width, centerHeight, 0x11, color));
        poseStack.popPose();
    }

    private static SubmitNodeCollector.CustomGeometryRenderer renderRhombus(final float width, final float height, final int alpha, final int color) {
        return (pose, vertexConsumer) -> NarakaRenderUtils.renderRhombus(pose, vertexConsumer, width, height, alpha, color);
    }

    @Override
    public void submit(ShinyEffectRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.rotation));
        submitShiny(renderState.ageInTicks, renderState.lifetime, renderState.scale, renderState.isVertical, renderState.color, poseStack, nodeCollector, cameraRenderState);
        poseStack.popPose();
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }
}
