package com.yummy.naraka.client.renderer.entity;

import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.AbsoluteHerobrineRenderState;
import com.yummy.naraka.world.entity.AbsoluteHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class AbsoluteHerobrineRenderer extends LivingEntityRenderer<AbsoluteHerobrine, AbsoluteHerobrineRenderState, HerobrineModel<AbsoluteHerobrineRenderState>> {
    public AbsoluteHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE), "absolute_herobrine"), 0);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(AbsoluteHerobrineRenderState renderState) {
        return NarakaTextures.HEROBRINE;
    }

    @Override
    public AbsoluteHerobrineRenderState createRenderState() {
        return new AbsoluteHerobrineRenderState();
    }

    @Override
    public void extractRenderState(AbsoluteHerobrine livingEntity, AbsoluteHerobrineRenderState renderState, float partialTicks) {
        super.extractRenderState(livingEntity, renderState, partialTicks);
        renderState.setupAnimationStates(livingEntity);
        renderState.lightCoords = 0;
    }

    @Override
    protected int getModelTint(AbsoluteHerobrineRenderState renderState) {
        return 0;
    }

    @Override
    protected boolean shouldShowName(AbsoluteHerobrine livingEntity, double d) {
        return false;
    }
}
