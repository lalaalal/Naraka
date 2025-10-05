package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.FlatImageRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.MagicCircle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public class MagicCircleRenderer extends EntityRenderer<MagicCircle, FlatImageRenderState> {
    public MagicCircleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public FlatImageRenderState createRenderState() {
        return new FlatImageRenderState();
    }

    @Override
    public void extractRenderState(MagicCircle entity, FlatImageRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.yRot = entity.getYRot(partialTick);
        reusedState.scale = entity.getScale(partialTick);
    }

    @Override
    public void submit(FlatImageRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.scale(renderState.scale, renderState.scale, renderState.scale);
        poseStack.mulPose(Axis.YN.rotation(renderState.yRot));
        submitNodeCollector.submitCustomGeometry(poseStack, RenderType.entityCutout(NarakaTextures.MAGIC_CIRCLE), (pose, vertexConsumer) -> {
            NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1, Direction.Axis.Y);
        });
        poseStack.popPose();
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
    }
}
