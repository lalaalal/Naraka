package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.renderer.entity.ShadowHerobrineRenderer;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import com.yummy.naraka.config.NarakaConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineHeadLayer extends RenderLayer<ShadowHerobrineRenderState, AbstractHerobrineModel<ShadowHerobrineRenderState>> {
    public ShadowHerobrineHeadLayer(ShadowHerobrineRenderer renderer) {
        super(renderer);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int packedLight, ShadowHerobrineRenderState renderState, float yRot, float xRot) {
        if (renderState.finalModel)
            return;
        poseStack.pushPose();
        int color = NarakaConfig.CLIENT.shadowHerobrineColor.getValue().pack();
        RenderType renderType = RenderTypes.entityCutout(NarakaTextures.SHADOW_HEROBRINE_HEAD);
        submitNodeCollector.submitModel(getParentModel(), renderState, poseStack, renderType, packedLight, OverlayTexture.NO_OVERLAY, color, null, renderState.outlineColor, null);
        poseStack.popPose();
    }
}
