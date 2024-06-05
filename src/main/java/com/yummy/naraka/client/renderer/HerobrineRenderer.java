package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.entity.Herobrine;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class HerobrineRenderer extends MobRenderer<Herobrine, HerobrineModel<Herobrine>> {
    public HerobrineRenderer(EntityRendererProvider.Context context) {
        this(context, NarakaModelLayers.HEROBRINE);
    }

    public HerobrineRenderer(EntityRendererProvider.Context context, ModelLayerLocation layerLocation) {
        super(context, new HerobrineModel<>(context.bakeLayer(layerLocation)), 0.5f);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    public ResourceLocation getTextureLocation(Herobrine entity) {
        return NarakaTextures.HEROBRINE;
    }

    @Override
    public void render(Herobrine entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
