package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineCrackLayer extends RenderLayer<ShadowHerobrineRenderState, AbstractHerobrineModel<ShadowHerobrineRenderState>> {
    private static final ResourceLocation[] CRACK_TEXTURES = {
            NarakaTextures.SHADOW_HEROBRINE_20,
            NarakaTextures.SHADOW_HEROBRINE_40,
            NarakaTextures.SHADOW_HEROBRINE_60,
            NarakaTextures.SHADOW_HEROBRINE_80,
    };

    public ShadowHerobrineCrackLayer(RenderLayerParent<ShadowHerobrineRenderState, AbstractHerobrineModel<ShadowHerobrineRenderState>> renderer) {
        super(renderer);
    }

    private ResourceLocation getTexture(ShadowHerobrineRenderState renderState) {
        return CRACK_TEXTURES[Math.clamp(renderState.crack, 0, 3)];
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector nodeCollector, int packedLight, ShadowHerobrineRenderState renderState, float yRot, float xRot) {
        if (renderState.crack > 3)
            return;
        poseStack.pushPose();
        AbstractHerobrineModel<ShadowHerobrineRenderState> model = getParentModel();
        RenderType renderType = RenderType.entityTranslucent(getTexture(renderState));
        nodeCollector.submitModel(model, renderState, poseStack, renderType, packedLight, OverlayTexture.NO_OVERLAY, -1, null, 0, null);
        poseStack.popPose();
    }
}
