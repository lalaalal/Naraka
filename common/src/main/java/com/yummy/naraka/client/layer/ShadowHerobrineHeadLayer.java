package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.ShadowHerobrineRenderer;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import com.yummy.naraka.config.NarakaConfig;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class ShadowHerobrineHeadLayer extends RenderLayer<ShadowHerobrineRenderState, HerobrineModel<ShadowHerobrineRenderState>> {
    public ShadowHerobrineHeadLayer(ShadowHerobrineRenderer renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ShadowHerobrineRenderState renderState, float yRot, float xRot) {
        poseStack.pushPose();
        int color = NarakaConfig.CLIENT.shadowHerobrineColor.getValue().pack();
        RenderType renderType = RenderType.entityCutout(NarakaTextures.SHADOW_HEROBRINE_HEAD);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }
}
