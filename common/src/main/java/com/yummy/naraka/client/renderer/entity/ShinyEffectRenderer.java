package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.renderer.entity.state.ShinyEffectRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.ShinyEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.world.entity.Entity;

@Environment(EnvType.CLIENT)
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

    @Override
    public void submit(ShinyEffectRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(renderState.rotation));
        submitShiny(renderState.ageInTicks, renderState.lifetime, renderState.scale, renderState.isVertical, renderState.color, poseStack, nodeCollector);
        poseStack.popPose();
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    public static void submitShiny(float tick, int lifetime, float scale, boolean isVertical, int color, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        Minecraft minecraft = Minecraft.getInstance();
        Entity camera = minecraft.getCameraEntity();
        if (camera != null)
            submitShiny(tick, lifetime, scale, camera.getYRot() + 180, isVertical, color, poseStack, submitNodeCollector);
    }

    public static void submitShiny(float tick, int lifetime, float scale, float yRot, boolean isVertical, int color, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        if (tick < 0 || tick > lifetime)
            return;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YN.rotationDegrees(yRot));
        if (isVertical)
            poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            renderShiny(pose, vertexConsumer, tick, lifetime, color);
        });

        poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            renderShiny(pose, vertexConsumer, tick, lifetime, color);
        });
        poseStack.popPose();
    }

    private static void renderShiny(PoseStack.Pose pose, VertexConsumer vertexConsumer, float tick, int lifetime, int color) {
        float width = NarakaUtils.interpolate(tick / lifetime, 0, 20, NarakaUtils::fastStepIn);
        float height = NarakaUtils.interpolate(tick / lifetime, 0.1f, 0, NarakaUtils::fastStepOut);

        NarakaRenderUtils.renderRhombus(pose, vertexConsumer, width, height, 0xff, 0xffffff);

        float centerWidth = Math.min(0.5f, width);
        float centerHeight = NarakaUtils.interpolate(tick / lifetime, 0.5f, 0, NarakaUtils::fastStepOut);
        int alpha = 0xff;
        while (centerWidth < width) {
            NarakaRenderUtils.renderRhombus(pose, vertexConsumer, centerWidth, centerHeight, alpha, color);
            centerWidth *= 2;
            alpha = (int) (alpha * 0.75f);
            centerHeight += 0.05f;
        }
        NarakaRenderUtils.renderRhombus(pose, vertexConsumer, width, centerHeight, 0x11, color);
    }
}
