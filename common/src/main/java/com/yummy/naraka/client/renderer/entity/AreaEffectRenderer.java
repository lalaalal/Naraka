package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.AreaEffectRenderState;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AreaEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class AreaEffectRenderer extends EntityRenderer<AreaEffect, AreaEffectRenderState> {
    public AreaEffectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public AreaEffectRenderState createRenderState() {
        return new AreaEffectRenderState();
    }

    @Override
    public void extractRenderState(AreaEffect entity, AreaEffectRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.lifetime = entity.getLifetime();
        reusedState.color = entity.getColor();
        reusedState.maxAlpha = entity.getMaxAlpha();
        reusedState.xWidth = entity.getXWidth();
        reusedState.zWidth = entity.getZWidth();
        reusedState.index = entity.getIndex();
    }

    @Override
    public boolean shouldRender(AreaEffect livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected boolean affectedByCulling(AreaEffect display) {
        return false;
    }

    private RenderType getRenderType(AreaEffectRenderState renderState) {
        if (renderState.maxAlpha == 0xff) {
            if (NarakaClientContext.SHADER_ENABLED.getValue())
                return RenderTypes.entityTranslucent(NarakaTextures.AREA_EFFECT);
            return RenderTypes.debugQuads();
        }
        return RenderTypes.lightning();
    }

    @Override
    public void submit(AreaEffectRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        int alpha = (int) NarakaUtils.interpolate(renderState.ageInTicks / renderState.lifetime, 0, renderState.maxAlpha, this::fadeInOut);
        poseStack.pushPose();
        nodeCollector.order(renderState.index).submitCustomGeometry(poseStack, getRenderType(renderState), (pose, vertexConsumer) -> {
            float x = renderState.xWidth / 2;
            float z = renderState.zWidth / 2;
            vertexConsumer.addVertex(pose, x, 0.1f, z)
                    .setUv(0, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setColor(ARGB.color(alpha, renderState.color))
                    .setNormal(pose, 0, 1, 0);
            vertexConsumer.addVertex(pose, x, 0.1f, -z)
                    .setUv(0, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setColor(ARGB.color(alpha, renderState.color))
                    .setNormal(pose, 0, 1, 0);
            vertexConsumer.addVertex(pose, -x, 0.1f, -z)
                    .setUv(1, 0)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setColor(ARGB.color(alpha, renderState.color))
                    .setNormal(pose, 0, 1, 0);
            vertexConsumer.addVertex(pose, -x, 0.1f, z)
                    .setUv(1, 1)
                    .setOverlay(OverlayTexture.NO_OVERLAY)
                    .setLight(LightTexture.FULL_BRIGHT)
                    .setColor(ARGB.color(alpha, renderState.color))
                    .setNormal(pose, 0, 1, 0);
        });

        poseStack.popPose();

        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    private float fadeInOut(float x) {
        if (x < 0.125f)
            return Mth.sin(4 * Mth.PI * x);
        if (x > 0.875f)
            return -Mth.sin(4 * Mth.PI * x);
        return 1;
    }
}
