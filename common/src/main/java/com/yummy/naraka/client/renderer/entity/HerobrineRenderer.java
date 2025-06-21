package com.yummy.naraka.client.renderer.entity;

import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineScarfLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.FinalHerobrineModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.HerobrineRenderState;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends AbstractHerobrineRenderer<Herobrine, HerobrineRenderState, AbstractHerobrineModel<HerobrineRenderState>> {
    private final AbstractHerobrineModel<HerobrineRenderState> afterimageModel;

    public HerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, defaultModel(context, HerobrineModel::new), finalModel(context, FinalHerobrineModel::new), 0.5f);
        this.afterimageModel = new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE));
    }

    @Override
    protected void addLayers(EntityRendererProvider.Context context) {
        this.addLayer(new HerobrineScarfLayer<>(this, context));
        super.addLayers(context);
    }

    @Override
    protected boolean isFinalModel(Herobrine entity) {
        return entity.getPhase() == 3;
    }

    @Override
    public HerobrineRenderState createRenderState() {
        return new HerobrineRenderState();
    }

    @Override
    public void extractRenderState(Herobrine herobrine, HerobrineRenderState renderState, float partialTicks) {
        renderState.phase = herobrine.getPhase();

        super.extractRenderState(herobrine, renderState, partialTicks);
    }

    @Override
    protected float getShadowRadius(HerobrineRenderState renderState) {
        if (renderState.phase == 3)
            return 0.7f * renderState.scale;
        return super.getShadowRadius(renderState);
    }

    @Override
    public ResourceLocation getTextureLocation(HerobrineRenderState renderState) {
        if (renderState.finalModel)
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.HEROBRINE;
    }

    @Override
    protected AbstractHerobrineModel<HerobrineRenderState> getAfterimageModel(HerobrineRenderState renderState) {
        if (renderState.finalModel)
            return finalModel;
        return afterimageModel;
    }

    @Override
    protected ResourceLocation getAfterimageTexture(HerobrineRenderState renderState) {
        if (renderState.phase == 3)
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }
}
