package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.world.entity.MassiveLightning;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.resources.ResourceLocation;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public class MassiveLightningRenderer extends EntityRenderer<MassiveLightning> {
    public MassiveLightningRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    @Deprecated
    public ResourceLocation getTextureLocation(MassiveLightning entity) {
        return TextureAtlas.LOCATION_BLOCKS;
    }

    @Override
    public boolean shouldRender(MassiveLightning livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(MassiveLightning entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float size = entity.getSize(partialTick);
        float ageInTicks = entity.tickCount + partialTick;
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotation(ageInTicks * 0.05f));
        RenderType renderType = RenderType.lightning();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        PoseStack.Pose pose = poseStack.last();
        pillar(vertexConsumer, pose, size * 0.6f, size * 0.6f, 1, 123, 0x66fafafa);
        pillar(vertexConsumer, pose, size * 0.7f, size * 0.7f, 0, 122, 0x55ababab);
        pillar(vertexConsumer, pose, size * 0.8f, size * 0.8f, 0, 121, 0x44ababab);
        pillar(vertexConsumer, pose, size * 0.9f, size * 0.9f, 0, 120, 0x33ababab);
        pillar(vertexConsumer, pose, size, size, 0, 120, 0x668308e4);

        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    private void pillar(VertexConsumer vertexConsumer, PoseStack.Pose pose, float innerSize, float outerSize, float minY, float maxY, int color) {
        plane(vertexConsumer, pose, new Vector3f(-innerSize, minY, outerSize), new Vector3f(innerSize, maxY, outerSize), color);
        plane(vertexConsumer, pose, new Vector3f(-innerSize, maxY, -outerSize), new Vector3f(innerSize, minY, -outerSize), color);
        plane(vertexConsumer, pose, new Vector3f(outerSize, minY, innerSize), new Vector3f(outerSize, maxY, -innerSize), color);
        plane(vertexConsumer, pose, new Vector3f(-outerSize, maxY, innerSize), new Vector3f(-outerSize, minY, -innerSize), color);
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
