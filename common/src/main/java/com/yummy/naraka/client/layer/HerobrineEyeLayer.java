package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class HerobrineEyeLayer<T extends AbstractHerobrineRenderState> extends RenderLayer<T, HerobrineModel<T>> {
    public HerobrineEyeLayer(RenderLayerParent<T, HerobrineModel<T>> renderer) {
        super(renderer);
    }

    private RenderType getRenderType() {
        return RenderType.entityTranslucent(NarakaTextures.HEROBRINE_EYE);
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource buffer, int packedLight, T renderState, float yRot, float xRot) {
        VertexConsumer vertexConsumer = buffer.getBuffer(getRenderType());
        HerobrineModel<T> model = getParentModel();
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xffffffff);
    }
}
