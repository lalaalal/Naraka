package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import com.yummy.naraka.client.renderer.entity.state.AfterimageRenderState;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.animation.AnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public abstract class AbstractHerobrineRenderer<T extends AbstractHerobrine, S extends AbstractHerobrineRenderState, M extends AbstractHerobrineModel<S>>
        extends AfterimageEntityRenderer<T, S, M> {

    protected AbstractHerobrineRenderer(EntityRendererProvider.Context context, M model, float shadowRadius) {
        super(context, model, shadowRadius);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    protected boolean shouldShowName(T livingEntity, double d) {
        return false;
    }

    @Override
    public void extractRenderState(T entity, S renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);
        renderState.isShadow = entity.isShadow;
        renderState.isStaggering = entity.getCurrentAnimation().equals(AnimationLocations.STAGGERING);
        renderState.isIdle = true;
        renderState.setAfterimages(entity, partialTicks);
        renderState.setAnimationVisitor(entity);
    }

    @Override
    protected void renderAfterimageLayer(S renderState, AfterimageRenderState afterimage, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int alpha) {
        VertexConsumer vertexConsumer = buffer.getBuffer(RenderType.entityTranslucent(NarakaTextures.HEROBRINE_EYE));
        int color = ARGB.white(Mth.clamp(alpha - 25, 0, 255));
        getAfterimageModel(renderState).renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color);
    }

    @Override
    @Nullable
    protected RenderType getRenderType(S renderState, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (renderState.isShadow)
            return RenderType.entityTranslucent(getTextureLocation(renderState));
        return super.getRenderType(renderState, bodyVisible, translucent, glowing);
    }

    @Override
    public ResourceLocation getTextureLocation(S renderState) {
        if (renderState.isShadow)
            return NarakaTextures.SHADOW_HEROBRINE;
        return NarakaTextures.HEROBRINE;
    }

    @Override
    protected ResourceLocation getAfterimageTexture(S renderState) {
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }

    @Override
    protected M getAfterimageModel(S renderState) {
        return model;
    }

    @Override
    protected int getModelTint(S renderState) {
        if (renderState.isShadow)
            return NarakaConfig.CLIENT.shadowHerobrineColor.getValue().withAlpha(0x88).pack();
        return super.getModelTint(renderState);
    }
}