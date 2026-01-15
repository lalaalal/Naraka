package com.yummy.naraka.client.renderer.entity;

import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.DiamondGolemModel;
import com.yummy.naraka.client.renderer.entity.state.SkillUsingMobRenderState;
import com.yummy.naraka.world.entity.DiamondGolem;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.Identifier;

@Environment(EnvType.CLIENT)
public class DiamondGolemRenderer extends MobRenderer<DiamondGolem, SkillUsingMobRenderState, DiamondGolemModel> {
    public DiamondGolemRenderer(EntityRendererProvider.Context context) {
        super(context, new DiamondGolemModel(context.bakeLayer(NarakaModelLayers.DIAMOND_GOLEM)), 2);
    }

    @Override
    public Identifier getTextureLocation(SkillUsingMobRenderState renderState) {
        return NarakaTextures.DIAMOND_GOLEM;
    }

    @Override
    public SkillUsingMobRenderState createRenderState() {
        return new SkillUsingMobRenderState();
    }

    @Override
    public void extractRenderState(DiamondGolem livingEntity, SkillUsingMobRenderState livingEntityRenderState, float partialTick) {
        super.extractRenderState(livingEntity, livingEntityRenderState, partialTick);
        livingEntityRenderState.setupAnimationStates(livingEntity);
    }
}
