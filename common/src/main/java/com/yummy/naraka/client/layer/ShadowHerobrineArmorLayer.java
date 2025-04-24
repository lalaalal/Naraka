package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.ShadowHerobrineRenderer;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineArmorLayer extends RenderLayer<ShadowHerobrineRenderState, HerobrineModel<ShadowHerobrineRenderState>> {
    private static final ResourceLocation TEXTURE_LOCATION = NarakaMod.mcLocation("textures/entity/creeper/creeper_armor.png");

    private final HerobrineModel<ShadowHerobrineRenderState> layerModel;

    public ShadowHerobrineArmorLayer(ShadowHerobrineRenderer renderer, EntityRendererProvider.Context context) {
        super(renderer);
        layerModel = new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.SHADOW_HEROBRINE_ARMOR));
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, ShadowHerobrineRenderState renderState, float yRot, float xRot) {
        layerModel.setupAnim(renderState);

        poseStack.pushPose();
        poseStack.scale(1.05f, 1.05f, 1.05f);
        float offset = (renderState.ageInTicks * 0.01f) % 1;
        RenderType renderType = RenderType.energySwirl(TEXTURE_LOCATION, offset, offset);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        layerModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, 0x44888888);
        poseStack.popPose();
    }
}
