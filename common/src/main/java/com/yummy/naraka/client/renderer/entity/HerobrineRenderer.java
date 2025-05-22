package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.layer.HerobrineScarfLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.HerobrineFinalModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends AbstractHerobrineRenderer<Herobrine, AbstractHerobrineModel<Herobrine>> {
    private final AbstractHerobrineModel<Herobrine> herobrineModel;
    private final AbstractHerobrineModel<Herobrine> herobrineFinalModel;
    private final AbstractHerobrineModel<Herobrine> afterimageModel;

    public HerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE)), 0.5f);
        this.herobrineModel = model;
        this.afterimageModel = new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE));
        this.herobrineFinalModel = new HerobrineFinalModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE_FINAL));

        addLayer(new HerobrineEyeLayer<>(this));
        addLayer(new HerobrineScarfLayer(this, context));
    }

    @Override
    public void render(Herobrine entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.getPhase() == 3) {
            model = herobrineFinalModel;
        } else {
            model = herobrineModel;
        }
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected AbstractHerobrineModel<Herobrine> getAfterimageModel(Herobrine entity) {
        if (entity.getPhase() == 3)
            return herobrineFinalModel;
        return afterimageModel;
    }

    @Override
    public ResourceLocation getTextureLocation(Herobrine entity) {
        if (entity.getPhase() == 3)
            return NarakaTextures.HEROBRINE_FINAL;
        return NarakaTextures.HEROBRINE;
    }
}