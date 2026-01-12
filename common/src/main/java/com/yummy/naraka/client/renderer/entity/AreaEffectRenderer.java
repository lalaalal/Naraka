package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.renderer.entity.state.AreaEffectRenderState;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.AreaEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
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

    @Override
    public void submit(AreaEffectRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        int alpha = (int) NarakaUtils.interpolate(renderState.ageInTicks / renderState.lifetime, 0, 0xff, this::fadeInOut);
        poseStack.pushPose();
        nodeCollector.order(renderState.index).submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            float x = renderState.xWidth / 2;
            float z = renderState.zWidth / 2;
            vertexConsumer.addVertex(pose, x, 0.1f, z)
                    .setColor(ARGB.color(alpha, renderState.color))
                    .setNormal(pose, 0, 1, 0);
            vertexConsumer.addVertex(pose, x, 0.1f, -z)
                    .setColor(ARGB.color(alpha, renderState.color))
                    .setNormal(pose, 0, 1, 0);
            vertexConsumer.addVertex(pose, -x, 0.1f, -z)
                    .setColor(ARGB.color(alpha, renderState.color))
                    .setNormal(pose, 0, 1, 0);
            vertexConsumer.addVertex(pose, -x, 0.1f, z)
                    .setColor(ARGB.color(alpha, renderState.color))
                    .setNormal(pose, 0, 1, 0);
        });

        poseStack.popPose();

        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    private float fadeInOut(float x) {
        return Mth.sin(Mth.PI * x);
    }
}
