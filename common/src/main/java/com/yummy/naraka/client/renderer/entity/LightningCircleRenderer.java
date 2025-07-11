package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.FlatImageRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.LightningCircle;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;

public class LightningCircleRenderer extends EntityRenderer<LightningCircle, FlatImageRenderState> {
    public LightningCircleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public FlatImageRenderState createRenderState() {
        return new FlatImageRenderState();
    }

    @Override
    public void extractRenderState(LightningCircle entity, FlatImageRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.scale = entity.getScale(partialTick);
        reusedState.alpha = Mth.lerp(reusedState.scale / LightningCircle.MAX_SCALE, 0.8f, 0);
    }

    @Override
    public boolean shouldRender(LightningCircle livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected boolean affectedByCulling(LightningCircle display) {
        return false;
    }

    @Override
    public void render(FlatImageRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(renderState, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        poseStack.translate(0, 0.0125, 0);
        poseStack.scale(renderState.scale, renderState.scale, renderState.scale);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucent(NarakaTextures.LIGHTNING_CIRCLE));
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ARGB.white(renderState.alpha), Direction.Axis.Y);

        poseStack.popPose();
    }
}
