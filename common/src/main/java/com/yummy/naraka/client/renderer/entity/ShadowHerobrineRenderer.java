package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineScarfLayer;
import com.yummy.naraka.client.layer.ShadowHerobrineHeadLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.FinalHerobrineModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.ColoredItemRenderer;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineRenderer extends AbstractHerobrineRenderer<ShadowHerobrine, ShadowHerobrineRenderState, AbstractHerobrineModel<ShadowHerobrineRenderState>> {
    public ShadowHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, defaultModel(context, HerobrineModel::new), finalModel(context, FinalHerobrineModel::new), 0.5f);
    }

    @Override
    protected void addLayers(EntityRendererProvider.Context context) {
        this.addLayer(new ShadowHerobrineHeadLayer(this));
        this.addLayer(new HerobrineScarfLayer<>(this, context));
        super.addLayers(context);
    }

    @Override
    public ShadowHerobrineRenderState createRenderState() {
        return new ShadowHerobrineRenderState();
    }

    @Override
    public void extractRenderState(ShadowHerobrine entity, ShadowHerobrineRenderState renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);
        renderState.hasRedOverlay = false;
        renderState.pickaxeLight = 0;
        renderState.alpha = entity.getAlpha();
        renderState.scarfAlpha = renderState.alpha;
        ColoredItemRenderer.setTemporaryColorForCurrent(Color.of(0).withAlpha(renderState.alpha));
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowHerobrineRenderState renderState) {
        if (renderState.finalModel)
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.SHADOW_HEROBRINE;
    }

    @Override
    public void render(ShadowHerobrineRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (renderState.finalModel && !renderState.displayPickaxe)
            return;
        super.render(renderState, poseStack, buffer, packedLight);
    }

    @Override
    protected int getModelTint(ShadowHerobrineRenderState renderState) {
        return NarakaConfig.CLIENT.shadowHerobrineColor.getValue().withAlpha(renderState.alpha).pack();
    }
}
