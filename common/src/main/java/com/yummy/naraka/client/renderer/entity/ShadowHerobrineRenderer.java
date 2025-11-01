package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.ShadowHerobrineCrackLayer;
import com.yummy.naraka.client.layer.ShadowHerobrineHeadLayer;
import com.yummy.naraka.client.model.AbstractHerobrineModel;
import com.yummy.naraka.client.model.FinalHerobrineModel;
import com.yummy.naraka.client.model.HerobrineModel;
import com.yummy.naraka.world.entity.ShadowHerobrine;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class ShadowHerobrineRenderer extends AbstractHerobrineRenderer<ShadowHerobrine, AbstractHerobrineModel<ShadowHerobrine>> {
    public ShadowHerobrineRenderer(EntityRendererProvider.Context context) {
        super(context, defaultModel(context, true, HerobrineModel::new), finalModel(context, true, FinalHerobrineModel::new), 0.5f);
    }

    @Override
    protected void addLayers(EntityRendererProvider.Context context) {
        this.addLayer(new ShadowHerobrineHeadLayer(this));
        this.addLayer(new ShadowHerobrineCrackLayer(this));
        super.addLayers(context);
    }

    @Override
    public ResourceLocation getTextureLocation(ShadowHerobrine shadowHerobrine) {
        if (shadowHerobrine.isFinalModel())
            return NarakaTextures.FINAL_HEROBRINE;
        return NarakaTextures.SHADOW_HEROBRINE;
    }

    @Override
    protected RenderType getRenderType(ShadowHerobrine livingEntity, boolean bodyVisible, boolean translucent, boolean glowing) {
        return RenderType.entityTranslucent(getTextureLocation(livingEntity));
    }

    @Override
    public void render(ShadowHerobrine entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        if (entity.isFinalModel() && !entity.displayPickaxe())
            return;
        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }
}
