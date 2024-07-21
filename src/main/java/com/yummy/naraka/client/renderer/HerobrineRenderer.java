package com.yummy.naraka.client.renderer;

import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends LivingEntityRenderer<Herobrine, HerobrineModel<Herobrine>> {
    public HerobrineRenderer(EntityRendererProvider.Context context) {
        this(context, NarakaModelLayers.HEROBRINE);
    }

    public HerobrineRenderer(EntityRendererProvider.Context context, ModelLayerLocation layerLocation) {
        super(context, new HerobrineModel<>(context.bakeLayer(layerLocation)), 0.5f);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    protected boolean shouldShowName(Herobrine pEntity) {
        return false;
    }

    @Override
    public ResourceLocation getTextureLocation(Herobrine entity) {
        return NarakaTextures.HEROBRINE;
    }
}
