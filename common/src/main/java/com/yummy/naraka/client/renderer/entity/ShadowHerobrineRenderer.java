package com.yummy.naraka.client.renderer.entity;

import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.ShadowHerobrineHeadLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineRenderer extends AbstractHerobrineRenderer<ShadowHerobrine, HerobrineModel<ShadowHerobrine>> {
    public ShadowHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE), true), 0.5f);
        addLayer(new ShadowHerobrineHeadLayer(this));
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowHerobrine entity) {
        return NarakaTextures.SHADOW_HEROBRINE;
    }
}
