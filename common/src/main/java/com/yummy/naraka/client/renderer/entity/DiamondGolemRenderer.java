package com.yummy.naraka.client.renderer.entity;

import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.DiamondGolemModel;
import com.yummy.naraka.world.entity.DiamondGolem;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.resources.ResourceLocation;

public class DiamondGolemRenderer extends MobRenderer<DiamondGolem, LivingEntityRenderState, DiamondGolemModel> {
    public DiamondGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new DiamondGolemModel(context.bakeLayer(ModelLayers.IRON_GOLEM)), 2);
    }

    @Override
    public ResourceLocation getTextureLocation(LivingEntityRenderState renderState) {
        return NarakaTextures.DIAMOND_GOLEM;
    }

    @Override
    public LivingEntityRenderState createRenderState() {
        return new LivingEntityRenderState();
    }
}
