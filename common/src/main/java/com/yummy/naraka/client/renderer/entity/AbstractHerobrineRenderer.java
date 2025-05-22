package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Afterimage;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractHerobrineRenderer<T extends AbstractHerobrine, M extends AbstractHerobrineModel<T>> extends AfterimageEntityRenderer<T, M> {
    public AbstractHerobrineRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
    }

    @Override
    protected boolean shouldShowName(T entity) {
        return false;
    }

    @Override
    @Nullable
    protected RenderType getRenderType(T herobrine, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (herobrine.isShadow)
            return RenderType.entityTranslucent(getTextureLocation(herobrine));
        return super.getRenderType(herobrine, bodyVisible, translucent, glowing);
    }

    @Override
    protected void renderAfterimageLayer(T herobrine, Afterimage afterimage, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int alpha) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(NarakaTextures.HEROBRINE_EYE));
        int color = Color.of(0xffffff).withAlpha(alpha).pack();
        getAfterimageModel(herobrine).renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color);
    }

    @Override
    protected M getAfterimageModel(T entity) {
        return model;
    }

    @Override
    protected ResourceLocation getAfterimageTexture(T entity) {
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }
}
