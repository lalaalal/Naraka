package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.Herobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends AfterimageEntityRenderer<Herobrine, HerobrineModel<Herobrine>> {
    public HerobrineRenderer(EntityRendererProvider.Context context) {
        this(context, NarakaModelLayers.HEROBRINE);
    }

    public HerobrineRenderer(EntityRendererProvider.Context context, ModelLayerLocation layerLocation) {
        super(context, () -> new HerobrineModel<>(context.bakeLayer(layerLocation)), 0.5f);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    protected boolean shouldShowName(Herobrine herobrine) {
        return false;
    }

    @Override
    protected void renderAfterimageLayer(Herobrine herobrine, Afterimage afterimage, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int alpha) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(NarakaTextures.HEROBRINE_EYE));
        int color = Color.of(0xffffff).withAlpha(alpha).pack();
        afterimageModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
    }

    @Override
    public ResourceLocation getTextureLocation(Herobrine herobrine) {
        return NarakaTextures.HEROBRINE;
    }

    @Override
    protected ResourceLocation getAfterimageTexture(Herobrine entity, boolean outline) {
        return outline ? NarakaTextures.HEROBRINE_AFTERIMAGE_OUTLINE : NarakaTextures.HEROBRINE_AFTERIMAGE;
    }
}