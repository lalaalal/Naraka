package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineCrackLayer extends RenderLayer<ShadowHerobrineRenderState, AbstractHerobrineModel<ShadowHerobrineRenderState>> {
    private static final Identifier[] CRACK_TEXTURES = {
            NarakaTextures.SHADOW_HEROBRINE_25,
            NarakaTextures.SHADOW_HEROBRINE_50,
            NarakaTextures.SHADOW_HEROBRINE_75
    };

    public ShadowHerobrineCrackLayer(RenderLayerParent<ShadowHerobrineRenderState, AbstractHerobrineModel<ShadowHerobrineRenderState>> renderer) {
        super(renderer);
    }

    private Identifier getTexture(ShadowHerobrineRenderState renderState) {
        return CRACK_TEXTURES[Math.clamp(renderState.crack, 0, CRACK_TEXTURES.length - 1)];
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, ShadowHerobrineRenderState renderState, float yRot, float xRot) {
        if (renderState.crack > CRACK_TEXTURES.length - 1)
            return;
        poseStack.pushPose();
        AbstractHerobrineModel<ShadowHerobrineRenderState> model = getParentModel();
        RenderType renderType = RenderType.entityTranslucent(getTexture(renderState));
        nodeCollector.submitModel(model, renderState, poseStack, renderType, packedLight, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
        poseStack.popPose();
    }
}
