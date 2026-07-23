package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.LightCoordsUtil;

public class HerobrineEyeLayer<S extends AbstractHerobrineRenderState, M extends AbstractHerobrineModel<S>> extends RenderLayer<S, M> {
    public HerobrineEyeLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    private RenderType getRenderType(S renderState) {
        if (renderState.eyeAlpha < 1)
            return RenderTypes.entityTranslucentEmissive(renderState.eyeTexture);
        return RenderTypes.eyes(renderState.eyeTexture);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, S renderState, float yRot, float xRot) {
        submitNodeCollector.order(1).submitModel(getParentModel(), renderState,
                poseStack, getRenderType(renderState),
                LightCoordsUtil.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ARGB.white(renderState.eyeAlpha), null, renderState.outlineColor, null
        );
    }
}
