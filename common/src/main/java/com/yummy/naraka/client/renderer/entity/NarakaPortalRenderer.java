package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.NarakaPortal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class NarakaPortalRenderer extends EntityRenderer<NarakaPortal> {
    public NarakaPortalRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(NarakaPortal entity) {
        return NarakaTextures.AREA_EFFECT;
    }

    @Override
    public boolean shouldRender(NarakaPortal livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    private RenderType getRenderType() {
        if (NarakaClientContext.SHADER_ENABLED.getValue())
            return RenderType.entityTranslucent(NarakaTextures.AREA_EFFECT);
        return NarakaRenderTypes.longinus();
    }

    @Override
    public void render(NarakaPortal entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float tick = entity.tickCount + partialTick;
        int lifetime = 100;
        float delta = Math.min(tick / lifetime, 1);

        float width = calculateWidth(delta);
        float height = NarakaUtils.interpolate(delta, 8, 4, NarakaUtils::fastStepOut);

        poseStack.pushPose();
        poseStack.mulPose(Axis.YN.rotationDegrees(entity.getViewYRot(partialTick)));

        VertexConsumer vertexConsumer = bufferSource.getBuffer(getRenderType());
        NarakaRenderUtils.renderRhombus(poseStack.last(), vertexConsumer, width, height, 0xbb, 0xffffff);
        poseStack.mulPose(Axis.YP.rotationDegrees(180));
        NarakaRenderUtils.renderRhombus(poseStack.last(), vertexConsumer, width, height, 0xbb, 0xffffff);
        poseStack.popPose();

        ShinyEffectRenderer.renderShiny(tick, lifetime - 10, 1, false, 0x888888, poseStack, bufferSource);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private float calculateWidth(float delta) {
        if (delta < 0.5f)
            return NarakaUtils.interpolate(delta * 2, 0, 1, NarakaUtils::fastStepIn);
        return NarakaUtils.interpolate((delta - 0.5f) * 2, 1, 0, NarakaUtils::fastStepOut);
    }
}
