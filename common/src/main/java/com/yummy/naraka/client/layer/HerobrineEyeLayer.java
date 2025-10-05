package com.yummy.naraka.client.layer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.Platform;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;

@Environment(EnvType.CLIENT)
public class HerobrineEyeLayer<S extends AbstractHerobrineRenderState, M extends AbstractHerobrineModel<S>> extends RenderLayer<S, M> {
    public HerobrineEyeLayer(RenderLayerParent<S, M> renderer) {
        super(renderer);
    }

    private RenderType getRenderType(S renderState) {
        if (Platform.getInstance().getModLoader() == Platform.ModLoader.NEO_FORGE)
            return RenderType.entityTranslucent(renderState.eyeTexture);
        return RenderType.entityTranslucentEmissive(renderState.eyeTexture);
    }

    @Override
    public void submit(PoseStack poseStack, SubmitNodeCollector submitNodeCollector, int light, S renderState, float yRot, float xRot) {
        submitNodeCollector.submitModel(getParentModel(), renderState,
                poseStack, getRenderType(renderState),
                light, OverlayTexture.NO_OVERLAY, ARGB.white(renderState.eyeAlpha), null
        );
    }
}
