package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineScarfLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.FinalHerobrineModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.HerobrineRenderState;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationLocations;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends AbstractHerobrineRenderer<Herobrine, HerobrineRenderState, AbstractHerobrineModel<HerobrineRenderState>> {
    private final AbstractHerobrineModel<HerobrineRenderState> afterimageModel;
    private final FinalHerobrineModel<HerobrineRenderState> dyingModel;

    public HerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, defaultModel(context, HerobrineModel::new), finalModel(context, FinalHerobrineModel::new), 0.5f);
        this.afterimageModel = new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE));
        this.dyingModel = new FinalHerobrineModel<>(context.bakeLayer(NarakaModelLayers.FINAL_HEROBRINE));
    }

    @Override
    protected void addLayers(EntityRendererProvider.Context context) {
        this.addLayer(new HerobrineScarfLayer<>(this, context));
        super.addLayers(context);
    }

    @Override
    public HerobrineRenderState createRenderState() {
        return new HerobrineRenderState();
    }

    @Override
    public void extractRenderState(Herobrine herobrine, HerobrineRenderState renderState, float partialTicks) {
        super.extractRenderState(herobrine, renderState, partialTicks);
        renderState.phase = herobrine.getPhase();
        renderState.hasRedOverlay = renderState.hasRedOverlay && !herobrine.isDeadOrDying();
        renderState.deathTime = -1;
        renderState.dead = herobrine.getCurrentAnimation().equals(HerobrineAnimationLocations.CHZZK);
        renderState.chzzkAnimationState = herobrine.chzzkAnimationState;
    }

    @Override
    public void submit(HerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.dead && NarakaClientContext.SHADER_ENABLED.getValue()) {
            submitChzzk(renderState, poseStack, submitNodeCollector, getChzzkRenderType(renderState, 0.001f, 0.01f), 1);
            submitChzzk(renderState, poseStack, submitNodeCollector, getChzzkRenderType(renderState, 0.002f, 0.005f), 2);
            submitChzzk(renderState, poseStack, submitNodeCollector, getChzzkRenderType(renderState, 0.0015f, 0.0025f), 3);
            renderState.lightCoords = 0;
        }
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    private RenderType getChzzkRenderType(HerobrineRenderState renderState, float uMultiplier, float vMultiplier) {
        return RenderType.energySwirl(NarakaTextures.LONGINUS, (renderState.ageInTicks * uMultiplier) % 1, (renderState.ageInTicks * vMultiplier) % 1);
    }

    private void submitChzzk(HerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderType renderType, int order) {
        poseStack.pushPose();
        dyingModel.applyHeadRotation(renderState);
        dyingModel.setupChzzkAnim(renderState.chzzkAnimationState, renderState.ageInTicks + (float) 0);
        this.setupRotations(renderState, poseStack, renderState.bodyRot, renderState.scale);
        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.501F, 0);
        submitNodeCollector.order(order).submitModel(
                dyingModel,
                renderState,
                poseStack,
                renderType,
                renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1,
                null,
                renderState.outlineColor,
                null
        );
        poseStack.popPose();
    }

    @Override
    protected float getShadowRadius(HerobrineRenderState renderState) {
        if (renderState.finalModel)
            return 0.7f * renderState.scale;
        return super.getShadowRadius(renderState);
    }

    @Override
    protected @Nullable RenderType getRenderType(HerobrineRenderState renderState, boolean bodyVisible, boolean translucent, boolean glowing) {
        if (renderState.dead) {
            if (NarakaClientContext.SHADER_ENABLED.getValue())
                return RenderType.entitySolid(NarakaTextures.LONGINUS);
            return NarakaRenderTypes.longinusCutout(NarakaTextures.FINAL_HEROBRINE);
        }
        return super.getRenderType(renderState, bodyVisible, translucent, glowing);
    }

    @Override
    public ResourceLocation getTextureLocation(HerobrineRenderState renderState) {
        if (renderState.finalModel)
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.HEROBRINE;
    }

    @Override
    protected AbstractHerobrineModel<HerobrineRenderState> getAfterimageModel(HerobrineRenderState renderState) {
        if (renderState.finalModel)
            return finalModel;
        return afterimageModel;
    }

    @Override
    protected ResourceLocation getAfterimageTexture(HerobrineRenderState renderState) {
        if (renderState.finalModel)
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }
}
