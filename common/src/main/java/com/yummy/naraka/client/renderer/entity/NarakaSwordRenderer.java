package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.layer.BeamEffectLayer;
import com.yummy.naraka.client.model.NarakaSwordModel;
import com.yummy.naraka.client.renderer.entity.state.NarakaSwordRenderState;
import com.yummy.naraka.world.entity.NarakaSword;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;

@Environment(EnvType.CLIENT)
public class NarakaSwordRenderer extends LightTailEntityRenderer<NarakaSword, NarakaSwordRenderState> implements RenderLayerParent<NarakaSwordRenderState, NarakaSwordModel> {
    private final NarakaSwordModel model;
    private final BeamEffectLayer<NarakaSwordRenderState, NarakaSwordModel> beamEffectLayer;

    public NarakaSwordRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new NarakaSwordModel(context.bakeLayer(NarakaModelLayers.NARAKA_SWORD));
        this.beamEffectLayer = new BeamEffectLayer<>(this);
    }

    @Override
    public NarakaSwordRenderState createRenderState() {
        return new NarakaSwordRenderState();
    }

    @Override
    public void extractRenderState(NarakaSword entity, NarakaSwordRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.updateBeamEffects(entity.beamEffects, reusedState.ageInTicks);
    }

    @Override
    public void submit(NarakaSwordRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        submitNodeCollector.submitModelPart(model.body(), poseStack, NarakaRenderTypes.longinus(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, -1, null);
        submitNodeCollector.submitModelPart(model.core(), poseStack, RenderTypes.entityCutout(NarakaTextures.NARAKA_SWORD), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, entityRenderState.tailColor, null);

        super.submit(entityRenderState, poseStack, submitNodeCollector, cameraRenderState);
        poseStack.popPose();

        beamEffectLayer.submit(poseStack, submitNodeCollector, entityRenderState.lightCoords, entityRenderState, entityRenderState.yRot, entityRenderState.zRot);
    }

    @Override
    public NarakaSwordModel getModel() {
        return model;
    }
}
