package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaModelLayers;
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
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
        if (renderState.dead) {
            submitChzzk(renderState, poseStack, submitNodeCollector, -2, renderState.lightCoords, 0x88ff0000);
            submitChzzk(renderState, poseStack, submitNodeCollector, 2.5f, renderState.lightCoords, 0x880000ff);
        }
    }

    private void submitChzzk(HerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, float tickOffset, int packedLight, int color) {
        poseStack.pushPose();
        poseStack.scale(0.935f, 0.935f, 0.935f);
        dyingModel.applyHeadRotation(renderState);
        dyingModel.setupChzzkAnim(renderState.chzzkAnimationState, renderState.ageInTicks + tickOffset);
        this.setupRotations(renderState, poseStack, renderState.bodyRot, renderState.scale);
        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.501F, 0);
        submitNodeCollector.submitModel(
                dyingModel,
                renderState,
                poseStack,
                RenderType.entityTranslucent(getTextureLocation(renderState)),
                packedLight, OverlayTexture.NO_OVERLAY, color,
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
