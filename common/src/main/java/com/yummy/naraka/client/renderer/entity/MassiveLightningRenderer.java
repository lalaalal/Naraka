package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.renderer.entity.state.MassiveLightningRenderState;
import com.yummy.naraka.world.entity.MassiveLightning;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
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
    protected boolean affectedByCulling(MassiveLightning display) {
        return false;
    }

    @Override
    public void render(MassiveLightningRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lightning());
        poseStack.mulPose(Axis.YP.rotation(renderState.ageInTicks * 0.05f));
        pillar(vertexConsumer, poseStack, renderState.size * 0.6f, renderState.size * 0.6f, 1, 123, 0x66fafafa);
        pillar(vertexConsumer, poseStack, renderState.size * 0.7f, renderState.size * 0.7f, 0, 122, 0x55ababab);
        pillar(vertexConsumer, poseStack, renderState.size * 0.8f, renderState.size * 0.8f, 0, 121, 0x44ababab);
        pillar(vertexConsumer, poseStack, renderState.size * 0.9f, renderState.size * 0.9f, 0, 120, 0x33ababab);
        pillar(vertexConsumer, poseStack, renderState.size, renderState.size, 0, 120, 0x665A1D8D);
        poseStack.popPose();

        super.render(renderState, poseStack, bufferSource, packedLight);
    }

    private void pillar(VertexConsumer vertexConsumer, PoseStack poseStack, float innerSize, float outerSize, float minY, float maxY, int color) {
        plane(vertexConsumer, poseStack.last(), new Vector3f(-innerSize, minY, outerSize), new Vector3f(innerSize, maxY, outerSize), color);
        plane(vertexConsumer, poseStack.last(), new Vector3f(-innerSize, maxY, -outerSize), new Vector3f(innerSize, minY, -outerSize), color);
        plane(vertexConsumer, poseStack.last(), new Vector3f(outerSize, minY, innerSize), new Vector3f(outerSize, maxY, -innerSize), color);
        plane(vertexConsumer, poseStack.last(), new Vector3f(-outerSize, maxY, innerSize), new Vector3f(-outerSize, minY, -innerSize), color);
    }

    private void plane(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vector3f min, Vector3f max, int color) {
        vertex(vertexConsumer, pose, new Vector3f(min.x, max.y, min.z), color);
        vertex(vertexConsumer, pose, new Vector3f(min.x, min.y, min.z), color);
        vertex(vertexConsumer, pose, new Vector3f(max.x, min.y, max.z), color);
        vertex(vertexConsumer, pose, new Vector3f(max.x, max.y, max.z), color);
    }

    private void vertex(VertexConsumer vertexConsumer, PoseStack.Pose pose, Vector3f position, int color) {
        vertexConsumer.addVertex(pose, position).setColor(color);
    }
}
