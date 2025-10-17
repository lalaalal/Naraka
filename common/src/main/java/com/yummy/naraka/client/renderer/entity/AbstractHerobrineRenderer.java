package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.world.entity.AbstractHerobrine;
import com.yummy.naraka.world.entity.Afterimage;
import com.yummy.naraka.world.item.NarakaItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public abstract class AbstractHerobrineRenderer<T extends AbstractHerobrine, M extends AbstractHerobrineModel<T>>
        extends AfterimageEntityRenderer<T, M> {
    protected final M defaultModel;
    protected final M finalModel;

    private final ItemRenderer itemRenderer;
    private final ItemStack pickaxe = NarakaItems.HEROBRINE_PICKAXE.get().getDefaultInstance();

    protected static <T extends AbstractHerobrine, M extends AbstractHerobrineModel<T>> M defaultModel(EntityRendererProvider.Context context, boolean forShadow, BiFunction<ModelPart, Boolean, M> constructor) {
        return constructor.apply(context.bakeLayer(NarakaModelLayers.HEROBRINE), forShadow);
    }

    protected static <T extends AbstractHerobrine, M extends AbstractHerobrineModel<T>> M finalModel(EntityRendererProvider.Context context, boolean forShadow, BiFunction<ModelPart, Boolean, M> constructor) {
        return constructor.apply(context.bakeLayer(NarakaModelLayers.FINAL_HEROBRINE), forShadow);
    }

    protected AbstractHerobrineRenderer(EntityRendererProvider.Context context, M defaultModel, M finalModel, float shadowRadius) {
        super(context, defaultModel, shadowRadius);
        this.defaultModel = defaultModel;
        this.finalModel = finalModel;
        this.itemRenderer = context.getItemRenderer();

        this.addLayers(context);
    }

    protected void addLayers(EntityRendererProvider.Context context) {
        this.addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    protected boolean shouldShowName(T entity) {
        return false;
    }

    protected void setupModelByRenderState(T entity) {
        if (entity.isFinalModel()) {
            this.model = finalModel;
        } else {
            this.model = defaultModel;
        }
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        setupModelByRenderState(entity);
        poseStack.pushPose();
        poseStack.scale(0.935f, 0.935f, 0.935f);
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();

        if (entity.isFinalModel() && entity.displayPickaxe()) {
            renderPickaxe(entity, partialTicks, poseStack, buffer, model.root(), model.main(), model.upperBody(), model.rightArm(), model.rightHand(), model.rightHand().getChild("pickaxe"));
            renderPickaxe(entity, partialTicks, poseStack, buffer, model.root().getChild("independent_pickaxe"));
        }
    }

    @Override
    protected float getFlipDegrees(T livingEntity) {
        return 0;
    }

    private void renderPickaxe(T entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, ModelPart... parts) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F - entity.getViewYRot(partialTick)));
        poseStack.translate(0, 1.4, 0);
        NarakaPickaxeRenderer.applyTransformAndRotate(poseStack, parts);
        poseStack.mulPose(Axis.XP.rotationDegrees(90));
        poseStack.mulPose(Axis.ZP.rotationDegrees(225));
        poseStack.translate(0.5, 0.5, 0);
        poseStack.scale(4, 4, 1);
        itemRenderer.renderStatic(pickaxe, ItemDisplayContext.NONE, getPickaxeLight(entity), OverlayTexture.NO_OVERLAY, poseStack, bufferSource, null, 0);
        poseStack.popPose();
    }

    private int getPickaxeLight(T entity) {
        if (entity.isShadow)
            return 0;
        return LightTexture.FULL_BRIGHT;
    }

    @Override
    protected void renderAfterimageLayer(T entity, Afterimage afterimage, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int alpha) {
        poseStack.pushPose();
        RenderType renderType = RenderType.entityTranslucent(NarakaTextures.HEROBRINE_EYE);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

        int color = FastColor.ARGB32.color(Mth.clamp(alpha - 25, 0, 255), 0xffffff);
        getAfterimageModel(entity).renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color);
        poseStack.popPose();
    }

    @Override
    @Nullable
    protected RenderType getRenderType(T livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (livingEntity.isShadow)
            return RenderType.entityTranslucent(getTextureLocation(livingEntity));
        return super.getRenderType(livingEntity, bodyVisible, translucent, glowing);
    }

    @Override
    protected ResourceLocation getAfterimageTexture(T entity) {
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }

    @Override
    protected M getAfterimageModel(T entity) {
        return model;
    }
}