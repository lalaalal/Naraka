package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.AbsoluteHerobrineRenderState;
import com.yummy.naraka.world.entity.AbsoluteHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class AbsoluteHerobrineRenderer extends LivingEntityRenderer<AbsoluteHerobrine, AbsoluteHerobrineRenderState, HerobrineModel<AbsoluteHerobrineRenderState>> {
    public AbsoluteHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, new HerobrineModel<>(context.bakeLayer(NarakaModelLayers.HEROBRINE), "absolute_herobrine"), 0);
        addLayer(new HerobrineEyeLayer<>(this));
    }

    @Override
    public Identifier getTextureLocation(AbsoluteHerobrineRenderState renderState) {
        return NarakaTextures.HEROBRINE;
    }

    @Override
    public AbsoluteHerobrineRenderState createRenderState() {
        return new AbsoluteHerobrineRenderState();
    }

    @Override
    public void extractRenderState(AbsoluteHerobrine livingEntity, AbsoluteHerobrineRenderState renderState, float partialTicks) {
        super.extractRenderState(livingEntity, renderState, partialTicks);
        renderState.setupAnimationStates(livingEntity);
        renderState.lightCoords = 0;
    }

    @Override
    public void submit(AbsoluteHerobrineRenderState livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0, 2.5, -1);
        ShinyEffectRenderer.submitShiny(1, 2, 0.25f, livingEntityRenderState.yRot + 180, false, 0xffffff, poseStack, submitNodeCollector);
        ShinyEffectRenderer.submitShiny(1, 2, 0.25f, livingEntityRenderState.yRot, false, 0xffffff, poseStack, submitNodeCollector);

        poseStack.popPose();

        super.submit(livingEntityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    @Override
    protected int getModelTint(AbsoluteHerobrineRenderState renderState) {
        return 0xff000000;
    }

    @Override
    protected boolean shouldShowName(AbsoluteHerobrine livingEntity, double d) {
        return false;
    }
}
