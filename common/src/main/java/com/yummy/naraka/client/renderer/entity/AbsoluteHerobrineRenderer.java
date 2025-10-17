package com.yummy.naraka.client.renderer.entity;

import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbsoluteHerobrineModel;
import com.yummy.naraka.world.entity.AbsoluteHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class AbsoluteHerobrineRenderer extends LivingEntityRenderer<AbsoluteHerobrine, AbsoluteHerobrineModel> {
    public AbsoluteHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new AbsoluteHerobrineModel(context.bakeLayer(NarakaModelLayers.ABSOLUTE_HEROBRINE)), 0);
    }

    @Override
    public ResourceLocation getTextureLocation(AbsoluteHerobrine entity) {
        return NarakaTextures.ABSOLUTE_HEROBRINE;
    }
}
