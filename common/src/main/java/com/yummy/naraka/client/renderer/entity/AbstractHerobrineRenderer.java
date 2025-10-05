package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.AbstractHerobrineRenderState;
import com.yummy.naraka.client.renderer.entity.state.AfterimageRenderState;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.item.ItemModelResolver;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.Function;

@Environment(EnvType.CLIENT)
public abstract class AbstractHerobrineRenderer<T extends AbstractHerobrine, S extends AbstractHerobrineRenderState, M extends AbstractHerobrineModel<S>>
        extends AfterimageEntityRenderer<T, S, M> {
    protected final M defaultModel;
    protected final M finalModel;

    private final ItemModelResolver itemModelResolver;
    private final ItemStack pickaxe = NarakaItems.HEROBRINE_PICKAXE.get().getDefaultInstance();

    protected static <S extends AbstractHerobrineRenderState, M extends AbstractHerobrineModel<S>> M defaultModel(EntityRendererProvider.Context context, Function<ModelPart, M> constructor) {
        return constructor.apply(context.bakeLayer(NarakaModelLayers.HEROBRINE));
    }

    protected static <S extends AbstractHerobrineRenderState, M extends AbstractHerobrineModel<S>> M finalModel(EntityRendererProvider.Context context, Function<ModelPart, M> constructor) {
        return constructor.apply(context.bakeLayer(NarakaModelLayers.FINAL_HEROBRINE));
    }

    protected AbstractHerobrineRenderer(EntityRendererProvider.Context context, M defaultModel, M finalModel, float shadowRadius) {
        super(context, defaultModel, shadowRadius);
        this.defaultModel = defaultModel;
        this.finalModel = finalModel;
        this.itemModelResolver = context.getItemModelResolver();

        this.addLayers(context);
    }

    protected void addLayers(EntityRendererProvider.Context context) {
        this.addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    protected boolean shouldShowName(T livingEntity, double d) {
        return false;
    }

    @Override
    public void extractRenderState(T entity, S renderState, float partialTicks) {
        super.extractRenderState(entity, renderState, partialTicks);
        renderState.finalModel = entity.isFinalModel();
        renderState.isShadow = entity.isShadow;
        renderState.isIdle = entity.shouldPlayIdleAnimation();
        renderState.eyeAlpha = entity.getEyeAlpha();
        renderState.doWalkAnimation = !renderState.finalModel;
        renderState.displayPickaxe = entity.displayPickaxe();

        renderState.setAfterimages(entity, partialTicks);
        renderState.setAnimationVisitor(entity);
        renderState.updateScarfRenderState(entity, partialTicks);

        if (renderState.finalModel) {
            renderState.eyeTexture = NarakaTextures.FINAL_HEROBRINE_EYE;
            this.model = finalModel;
        } else {
            renderState.eyeTexture = NarakaTextures.HEROBRINE_EYE;
            this.model = defaultModel;
        }

        itemModelResolver.updateForLiving(renderState.pickaxe, pickaxe, ItemDisplayContext.NONE, entity);
    }

    @Override
    public void render(S renderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.scale(0.935f, 0.935f, 0.935f);
        super.render(renderState, poseStack, buffer, packedLight);
        poseStack.popPose();

        if (renderState.finalModel && renderState.displayPickaxe) {
            renderPickaxe(renderState, poseStack, buffer, model.root(), model.main(), model.upperBody(), model.rightArm(), model.rightHand(), model.rightHand().getChild("pickaxe"));
            renderPickaxe(renderState, poseStack, buffer, model.root().getChild("independent_pickaxe"));
        }
    }

    private void renderPickaxe(S renderState, PoseStack poseStack, MultiBufferSource buffer, ModelPart... parts) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - renderState.bodyRot));
        poseStack.translate(0, 1.4, 0);
        NarakaPickaxeRenderer.applyTransformAndRotate(poseStack, parts);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(225));
        poseStack.translate(0.5, 0.5, 0);
        poseStack.scale(4, 4, 1);
        renderState.pickaxe.render(poseStack, buffer, renderState.pickaxeLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
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
    protected ResourceLocation getAfterimageTexture(S renderState) {
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }

    @Override
    protected M getAfterimageModel(S renderState) {
        return model;
    }
}