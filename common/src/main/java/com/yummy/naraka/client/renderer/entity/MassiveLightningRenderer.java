package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.renderer.entity.state.MassiveLightningRenderState;
import com.yummy.naraka.world.entity.MassiveLightning;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import org.joml.Vector3f;

public class MassiveLightningRenderer extends EntityRenderer<MassiveLightning, MassiveLightningRenderState> {
    public MassiveLightningRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public MassiveLightningRenderState createRenderState() {
        return new MassiveLightningRenderState();
    }

    @Override
    public void extractRenderState(MassiveLightning entity, MassiveLightningRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.size = entity.getSize(partialTick);
    }

    @Override
    public boolean shouldRender(MassiveLightning livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(MassiveLightningRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lightning());

        poseStack.pushPose();
        poseStack.mulPose(Axis.YN.rotation(renderState.ageInTicks * 0.2f));
        pillar(vertexConsumer, poseStack, renderState.size, 0, 120, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xff868686);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotation(renderState.ageInTicks * 0.175f));
        pillar(vertexConsumer, poseStack, renderState.size, 0, 120, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xff868686);
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.scale(1.1f, 1.1f, 1.1f);
        poseStack.mulPose(Axis.YP.rotation(renderState.ageInTicks * 0.075f));
        pillar(vertexConsumer, poseStack, renderState.size, 0, 120, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xff5A1D8D);
        poseStack.popPose();

        poseStack.popPose();

        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    private void pillar(VertexConsumer vertexConsumer, PoseStack poseStack, float size, float minY, float maxY, int packedLight, int packedOverlay, int color) {
        plane(vertexConsumer, poseStack.last(), new Vector3f(-size, minY, size), new Vector3f(size, maxY, size), packedLight, packedOverlay, color);
        plane(vertexConsumer, poseStack.last(), new Vector3f(-size, maxY, -size), new Vector3f(size, minY, -size), packedLight, packedOverlay, color);
        plane(vertexConsumer, poseStack.last(), new Vector3f(size, minY, size), new Vector3f(size, maxY, -size), packedLight, packedOverlay, color);
        plane(vertexConsumer, poseStack.last(), new Vector3f(-size, maxY, size), new Vector3f(-size, minY, -size), packedLight, packedOverlay, color);
    }

    private void plane(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vector3f min, Vector3f max, int packedLight, int packedOverlay, int color) {
        vertex(vertexConsumer, pose, new Vector3f(min.x, max.y, min.z), packedLight, packedOverlay, color);
        vertex(vertexConsumer, pose, new Vector3f(min.x, min.y, min.z), packedLight, packedOverlay, color);
        vertex(vertexConsumer, pose, new Vector3f(max.x, min.y, max.z), packedLight, packedOverlay, color);
        vertex(vertexConsumer, pose, new Vector3f(max.x, max.y, max.z), packedLight, packedOverlay, color);
    }

    private void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vector3f position, int packedLight, int packedOverlay, int color) {
        vertexConsumer.addVertex(pose, position).setUv(0, 0).setLight(packedLight).setOverlay(packedOverlay).setColor(color).setNormal(pose, 0, 1, 0);
    }
}
