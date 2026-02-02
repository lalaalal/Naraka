package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.BeamEffectLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.FinalHerobrineModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.world.entity.BeamEffect;
import com.yummy.naraka.world.entity.Herobrine;
import com.yummy.naraka.world.entity.data.BeamEffectsHelper;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public class HerobrineRenderer extends AbstractHerobrineRenderer<Herobrine, AbstractHerobrineModel<Herobrine>> {
    private final AbstractHerobrineModel<Herobrine> afterimageModel;
    private final FinalHerobrineModel<Herobrine> dyingModel;

    public HerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, defaultModel(context, false, HerobrineModel::new), finalModel(context, false, FinalHerobrineModel::new), 0.5f);
        this.afterimageModel = new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE), false);
        this.dyingModel = new FinalHerobrineModel<>(context.bakeLayer(NarakaModelLayers.FINAL_HEROBRINE), false);
    }

    @Override
    protected void addLayers(EntityRendererProvider.Context context) {
        super.addLayers(context);
        this.addLayer(new BeamEffectLayer<>(this, new Vector3f(3, -3, 3), new Vector3f(0, -0.5f, 0)));
    }

    @Override
    public void render(Herobrine entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        List<BeamEffect> beamEffects = BeamEffectsHelper.get(entity);
        entity.updateBeamEffects(beamEffects, entity.tickCount + partialTicks);
        if (entity.isDeadOrDying() && entity.deathTime > 60) {
            if (NarakaClientContext.SHADER_ENABLED.getValue())
                packedLight = 0;
            renderChzzk(entity, partialTicks + 10, poseStack, buffer, getChzzkRenderType(entity, partialTicks, 0.001f, 0.01f), packedLight);
            renderChzzk(entity, partialTicks + 30, poseStack, buffer, getChzzkRenderType(entity, partialTicks, 0.002f, 0.005f), packedLight);
        }
        poseStack.pushPose();
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
        poseStack.popPose();
    }

    private RenderType getChzzkRenderType(Herobrine herobrine, float partialTick, float uMultiplier, float vMultiplier) {
        float ageInTicks = herobrine.tickCount + partialTick;
        if (NarakaClientContext.SHADER_ENABLED.getValue())
            return RenderType.energySwirl(NarakaTextures.LONGINUS, (ageInTicks * uMultiplier) % 1, (ageInTicks * vMultiplier) % 1);
        return NarakaRenderTypes.longinusCutout(NarakaTextures.FINAL_HEROBRINE);
    }

    private void renderChzzk(Herobrine herobrine, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, RenderType renderType, int packedLight) {
        poseStack.pushPose();
        dyingModel.applyHeadRotation(herobrine.getYHeadRot(), herobrine.getViewXRot(partialTick));
        dyingModel.setupChzzkAnimation(herobrine.chzzkAnimationState, herobrine.tickCount + partialTick);
        this.setupRotations(herobrine, poseStack, getBob(herobrine, partialTick), herobrine.getViewYRot(partialTick), partialTick, herobrine.getScale());
        poseStack.scale(-1, -1, 1);
        poseStack.translate(0, -1.401F, 0);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        dyingModel.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        poseStack.popPose();
    }

    @Override
    protected float getShadowRadius(Herobrine herobrine) {
        if (herobrine.isFinalModel())
            return 0.7f * herobrine.getScale();
        return super.getShadowRadius(herobrine);
    }

    @Override
    public ResourceLocation getTextureLocation(Herobrine herobrine) {
        if (herobrine.isFinalModel())
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.HEROBRINE;
    }

    @Override
    protected AbstractHerobrineModel<Herobrine> getAfterimageModel(Herobrine herobrine) {
        if (herobrine.isFinalModel())
            return finalModel;
        return afterimageModel;
    }

    @Override
    protected ResourceLocation getAfterimageTexture(Herobrine herobrine) {
        if (herobrine.isFinalModel())
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.HEROBRINE_AFTERIMAGE;
    }
}
