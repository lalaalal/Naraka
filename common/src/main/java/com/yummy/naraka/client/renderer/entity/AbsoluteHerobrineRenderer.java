package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.HerobrineEyeLayer;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.entity.state.AbsoluteHerobrineRenderState;
import com.yummy.naraka.world.entity.AbsoluteHerobrine;
import com.yummy.naraka.world.item.SoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

import java.util.LinkedHashMap;

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
        renderState.absorbedSoulTypes = livingEntity.getAbsorbedSoulTypes();
        renderState.soulTypeAlpha = new LinkedHashMap<>(livingEntity.getSoulTypeAlpha());
        renderState.outlineColor = 0xffffffff;
    }

    @Override
    public void submit(AbsoluteHerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0, 2.5, -1);
        ShinyEffectRenderer.submitShiny(1, 2, 0.25f, renderState.yRot + 180, false, 0xffffff, poseStack, submitNodeCollector);
        ShinyEffectRenderer.submitShiny(1, 2, 0.25f, renderState.yRot, false, 0xffffff, poseStack, submitNodeCollector);
        submitColors(renderState, poseStack, submitNodeCollector);

        poseStack.popPose();

        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    private void submitColors(AbsoluteHerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        int index = 0;
        for (SoulType soulType : renderState.soulTypeAlpha.keySet()) {
            float alpha = renderState.soulTypeAlpha.getOrDefault(soulType, 0f);
            int halfIndex = index / 2;
            double xOffset = 0.6 * (halfIndex + 1);
            if (index % 2 == 0)
                xOffset *= -1;
            float scale = 0.066f - Mth.log2(halfIndex) * 0.03f;

            poseStack.pushPose();
            poseStack.translate(xOffset, 0, 0);
            poseStack.scale(4 + halfIndex, 1, 1);
            ShinyEffectRenderer.submitShiny(alpha * 50, 100, scale, renderState.yRot + 180, true, soulType.color, poseStack, submitNodeCollector);
            ShinyEffectRenderer.submitShiny(alpha * 50, 100, scale, renderState.yRot, true, soulType.color, poseStack, submitNodeCollector);

            poseStack.popPose();

            index++;
        }
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
