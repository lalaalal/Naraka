package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.layer.ShadowHerobrineArmorLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.Util;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer<T extends AbstractHerobrine> extends AfterimageEntityRenderer<T, HerobrineModel<T>> {
    public HerobrineRenderer(EntityRendererProvider.Context context) {
        this(context, NarakaModelLayers.HEROBRINE);
    }

    public static HerobrineRenderer<ShadowHerobrine> shadow(EntityRendererProvider.Context context) {
        return Util.make(
                new HerobrineRenderer<>(context, NarakaModelLayers.HEROBRINE),
                renderer -> renderer.addLayer(new ShadowHerobrineArmorLayer(renderer))
        );
    }

    protected HerobrineRenderer(EntityRendererProvider.Context context, ModelLayerLocation layerLocation) {
        super(context, () -> new HerobrineModel<>(context.bakeLayer(layerLocation)), 0.5f);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    protected boolean shouldShowName(T herobrine) {
        return false;
    }

    @Override
    public void render(T herobrine, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (herobrine.isShadow) {
            model.renderShadow();
        }
        super.render(herobrine, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    @Override
    protected void renderAfterimageLayer(T herobrine, Afterimage afterimage, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int alpha) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(NarakaTextures.HEROBRINE_EYE));
        int color = Color.of(0xffffff).withAlpha(alpha).pack();
        afterimageModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY, color);
    }

    @Override
    @Nullable
    protected RenderType getRenderType(T herobrine, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (herobrine.isShadow)
            return RenderType.entityTranslucent(getTextureLocation(herobrine));
        return super.getRenderType(herobrine, bodyVisible, translucent, glowing);
    }

    @Override
    public ResourceLocation getTextureLocation(T herobrine) {
        if (herobrine.isShadow)
            return NarakaTextures.SHADOW_HEROBRINE;
        return NarakaTextures.HEROBRINE;
    }

    @Override
    protected ResourceLocation getAfterimageTexture(T herobrine) {
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }
}