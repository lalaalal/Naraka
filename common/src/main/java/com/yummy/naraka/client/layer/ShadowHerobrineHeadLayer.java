package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.HerobrineRenderer;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

public class ShadowHerobrineHeadLayer extends RenderLayer<ShadowHerobrine, HerobrineModel<ShadowHerobrine>> {
    public ShadowHerobrineHeadLayer(HerobrineRenderer<ShadowHerobrine> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ShadowHerobrine livingEntity, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        int color = NarakaConfig.CLIENT.shadowHerobrineColor.getValue().pack();
        RenderType renderType = RenderType.entityCutout(NarakaTextures.SHADOW_HEROBRINE_HEAD);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        getParentModel().renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }
}
