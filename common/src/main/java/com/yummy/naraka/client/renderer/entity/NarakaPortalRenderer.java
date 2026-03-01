package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.NarakaPortalRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.NarakaPortal;
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

@Environment(EnvType.CLIENT)
public class NarakaPortalRenderer extends EntityRenderer<NarakaPortal, NarakaPortalRenderState> {
    public NarakaPortalRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(NarakaPortal livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public NarakaPortalRenderState createRenderState() {
        return new NarakaPortalRenderState();
    }

    private RenderType getRenderType() {
        if (NarakaClientContext.SHADER_ENABLED.getValue())
            return RenderTypes.entityTranslucent(NarakaTextures.AREA_EFFECT);
        return NarakaRenderTypes.longinus();
    }

    @Override
    public void extractRenderState(NarakaPortal entity, NarakaPortalRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.lifetime = 100;
        float delta = Math.min(reusedState.ageInTicks / reusedState.lifetime, 1);

        reusedState.yRot = entity.getViewYRot(partialTick);
        reusedState.width = calculateWidth(delta);
        reusedState.height = NarakaUtils.interpolate(delta, 8, 4, NarakaUtils::fastStepOut);
    }

    @Override
    public void submit(NarakaPortalRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YN.rotationDegrees(renderState.yRot));
        nodeCollector.submitCustomGeometry(poseStack, getRenderType(), (pose, vertexConsumer) -> {
            NarakaRenderUtils.renderRhombus(pose, vertexConsumer, renderState.width, renderState.height, LightTexture.FULL_BRIGHT, 0xbb, 0xffffff);
        });
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        nodeCollector.submitCustomGeometry(poseStack, getRenderType(), (pose, vertexConsumer) -> {
            NarakaRenderUtils.renderRhombus(pose, vertexConsumer, renderState.width, renderState.height, LightTexture.FULL_BRIGHT, 0xbb, 0xffffff);
        });
        poseStack.popPose();

        ShinyEffectRenderer.submitShiny(renderState.ageInTicks, renderState.lifetime - 10, 1, false, 0x888888, poseStack, nodeCollector);
        super.submit(renderState, poseStack, nodeCollector, cameraRenderState);
    }

    private float calculateWidth(float delta) {
        if (delta < 0.5f)
            return NarakaUtils.interpolate(delta * 2, 0, 1, NarakaUtils::fastStepIn);
        return NarakaUtils.interpolate((delta - 0.5f) * 2, 1, 0, NarakaUtils::fastStepOut);
    }
}
