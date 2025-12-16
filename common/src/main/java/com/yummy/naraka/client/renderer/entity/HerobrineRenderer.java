package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.BeamEffectLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.FinalHerobrineModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.HerobrineRenderState;
import com.yummy.naraka.world.entity.BeamEffect;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.animation.HerobrineAnimationIdentifiers;
import com.yummy.naraka.world.entity.data.BeamEffectsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.util.RandomSource;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

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
        super.addLayers(context);
        this.addLayer(new BeamEffectLayer<>(this, new Vector3f(3, -3, 3), new Vector3f(0, -0.5f, 0)));
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
        renderState.dead = herobrine.getCurrentAnimation().equals(HerobrineAnimationIdentifiers.CHZZK);
        renderState.chzzkAnimationState = herobrine.chzzkAnimationState;
        List<BeamEffect> beamEffects = BeamEffectsHelper.get(herobrine);
        renderState.updateBeamEffects(beamEffects, renderState.ageInTicks);
        renderState.translation = renderState.alpha == 0xff ? Vec3.ZERO : randomTranslation(herobrine);
    }

    private Vec3 randomTranslation(Herobrine herobrine) {
        RandomSource random = RandomSource.create(herobrine.tickCount);
        double x = random.nextDouble() * 0.5;
        double z = random.nextDouble() * 0.5;
        return new Vec3(x, 0, z);
    }

    @Override
    public void submit(HerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.dead) {
            if (NarakaClientContext.SHADER_ENABLED.getValue()) {
                submitChzzk(renderState, poseStack, submitNodeCollector, getChzzkRenderType(renderState, 0.001f, 0.01f), 1, 0);
                submitChzzk(renderState, poseStack, submitNodeCollector, getChzzkRenderType(renderState, 0.002f, 0.005f), 2, 0);
                submitChzzk(renderState, poseStack, submitNodeCollector, getChzzkRenderType(renderState, 0.0015f, 0.0025f), 3, 0);
                renderState.lightCoords = 0;
            } else {
                submitChzzk(renderState, poseStack, submitNodeCollector, NarakaRenderTypes.longinusCutout(getTextureLocation(renderState)), 1, 10);
            }
        }
        poseStack.pushPose();
        poseStack.translate(renderState.translation);
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
        poseStack.popPose();
    }

    private RenderType getChzzkRenderType(HerobrineRenderState renderState, float uMultiplier, float vMultiplier) {
        return RenderTypes.energySwirl(NarakaTextures.LONGINUS, (renderState.ageInTicks * uMultiplier) % 1, (renderState.ageInTicks * vMultiplier) % 1);
    }

    private void submitChzzk(HerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, RenderType renderType, int order, int tickOffset) {
        poseStack.pushPose();
        dyingModel.applyHeadRotation(renderState);
        dyingModel.setupChzzkAnim(renderState.chzzkAnimationState, renderState.ageInTicks + tickOffset);
        this.setupRotations(renderState, poseStack, renderState.bodyRot, renderState.scale);
        poseStack.scale(-0.935f, -0.935f, 0.935f);
        poseStack.translate(0, -1.501F, 0);
        submitNodeCollector.order(order).submitModelPart(
                dyingModel.root(),
                poseStack,
                renderType,
                renderState.lightCoords,
                OverlayTexture.NO_OVERLAY,
                null,
                -1,
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
    public Identifier getTextureLocation(HerobrineRenderState renderState) {
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
    protected Identifier getAfterimageTexture(HerobrineRenderState renderState) {
        if (renderState.finalModel)
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }
}
