package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.NarakaFireballModel;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.NarakaFireball;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class NarakaFireballRenderer extends EntityRenderer<NarakaFireball, EntityRenderState> {
    private final NarakaFireballModel model;

    public NarakaFireballRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new NarakaFireballModel(context.bakeLayer(NarakaModelLayers.NARAKA_FIREBALL));
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void extractRenderState(NarakaFireball entity, EntityRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.boundingBoxWidth *= 0.67f;
        reusedState.boundingBoxHeight *= 0.67f;
    }

    @Override
    public void submit(EntityRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();

        float rotation = renderState.ageInTicks * 5;
        poseStack.translate(0, 0.33, 0);
        poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), NarakaRenderUtils.SIN_45, 0.0F, NarakaRenderUtils.SIN_45));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * 2));

        RenderType renderType = model.renderType(NarakaTextures.NARAKA_FIREBALL);

        submitNodeCollector.submitModel(model, renderState, poseStack, renderType, renderState.lightCoords, OverlayTexture.NO_OVERLAY, -1, null);
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);

        poseStack.popPose();
    }
}
