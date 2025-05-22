package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.layer.ShadowHerobrineHeadLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineRenderer extends AbstractHerobrineRenderer<ShadowHerobrine, ShadowHerobrineRenderState, HerobrineModel<ShadowHerobrineRenderState>> {
    public ShadowHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE)), 0.5f);
        this.addLayer(new ShadowHerobrineHeadLayer(this));
    }

    @Override
    public ShadowHerobrineRenderState createRenderState() {
        return new ShadowHerobrineRenderState();
    }

    @Override
    public void extractRenderState(ShadowHerobrine entity, ShadowHerobrineRenderState renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);
        renderState.hasRedOverlay = false;
    }

    @Override
    public void render(ShadowHerobrineRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(renderState, poseStack, buffer, LightTexture.FULL_BRIGHT);
    }
}
