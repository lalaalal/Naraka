package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.ShadowHerobrineCrackLayer;
import com.yummy.naraka.client.layer.ShadowHerobrineHeadLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.FinalHerobrineModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.client.renderer.ItemRenderRegistry;
import com.yummy.naraka.client.renderer.entity.state.ShadowHerobrineRenderState;
import com.yummy.naraka.config.NarakaConfig;
import com.yummy.naraka.util.Color;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineRenderer extends AbstractHerobrineRenderer<ShadowHerobrine, ShadowHerobrineRenderState, AbstractHerobrineModel<ShadowHerobrineRenderState>> {
    public ShadowHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, defaultModel(context, HerobrineModel::new), finalModel(context, FinalHerobrineModel::new), 0.5f);
    }

    @Override
    protected void addLayers(EntityRendererProvider.Context context) {
        this.addLayer(new ShadowHerobrineHeadLayer(this));
        this.addLayer(new ShadowHerobrineCrackLayer(this));
        super.addLayers(context);
    }

    @Override
    public ShadowHerobrineRenderState createRenderState() {
        return new ShadowHerobrineRenderState();
    }

    @Override
    public void extractRenderState(ShadowHerobrine entity, ShadowHerobrineRenderState renderState, float partialTicks) {
        renderState.alpha = entity.getAlpha();
        if (entity.displayPickaxe())
            ItemRenderRegistry.setTemporaryColor(renderState.pickaxe, Color.of(0).withAlpha(renderState.alpha).pack());
        super.extractRenderState(entity, renderState, partialTicks);

        renderState.hasRedOverlay = false;
        renderState.pickaxeLight = 0;
        renderState.scarfAlpha = renderState.alpha;
        float healthRatio = entity.getHealth() / entity.getMaxHealth();
        renderState.crack = Mth.floor(healthRatio / 0.25f);
    }

    @Override
    public Identifier getTextureLocation(ShadowHerobrineRenderState renderState) {
        if (renderState.finalModel)
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.SHADOW_HEROBRINE;
    }

    @Override
    public void submit(ShadowHerobrineRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (renderState.finalModel && !renderState.displayPickaxe)
            return;
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    @Override
    protected int getModelTint(ShadowHerobrineRenderState renderState) {
        return NarakaConfig.CLIENT.shadowHerobrineColor.getValue().withAlpha(renderState.alpha).pack();
    }
}
