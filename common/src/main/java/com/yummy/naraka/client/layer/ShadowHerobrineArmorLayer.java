package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

public class ShadowHerobrineArmorLayer extends RenderLayer<ShadowHerobrine, HerobrineModel<ShadowHerobrine>> {
    private static final ResourceLocation TEXTURE_LOCATION = NarakaMod.mcLocation("textures/entity/creeper/creeper_armor.png");

    public ShadowHerobrineArmorLayer(RenderLayerParent<ShadowHerobrine, HerobrineModel<ShadowHerobrine>> renderer) {
        super(renderer);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ShadowHerobrine shadowHerobrine, float limbSwing, float limbSwingAmount, float partialTick, float ageInTicks, float netHeadYaw, float headPitch) {
        poseStack.pushPose();
        float offset = ((shadowHerobrine.tickCount + partialTick) * 0.01f) % 1;
        RenderType renderType = RenderType.energySwirl(TEXTURE_LOCATION, offset, offset);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        HerobrineModel<ShadowHerobrine> model = getParentModel();
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0x44888888);
        poseStack.popPose();
    }
}
