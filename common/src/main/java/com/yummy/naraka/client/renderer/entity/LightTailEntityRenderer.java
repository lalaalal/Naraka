package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.entity.state.LightTailEntityRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.LightTailEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

@Environment(EnvType.CLIENT)
public abstract class LightTailEntityRenderer<T extends LightTailEntity, S extends LightTailEntityRenderState> extends EntityRenderer<T, S> {
    protected LightTailEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public void extractRenderState(T entity, S reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.tailPositions = entity.getTailPositions()
                .stream()
                .map(position -> position.subtract(entity.position()))
                .map(NarakaRenderUtils::vector3f)
                .toList();
        reusedState.partialTranslation = entity.position()
                .subtract(entity.getPosition(partialTick));
        reusedState.tailColor = entity.getTailColor();
        reusedState.tailWidth = 0.15f;
    }

    @Override
    protected AABB getBoundingBoxForCulling(T entity) {
        AABB boundingBox = super.getBoundingBoxForCulling(entity);
        double maxX = boundingBox.maxX, maxY = boundingBox.maxY, maxZ = boundingBox.maxZ;
        double minX = boundingBox.minX, minY = boundingBox.minY, minZ = boundingBox.minZ;
        for (Vec3 position : entity.getTailPositions()) {
            maxX = Math.max(position.x, maxX);
            maxY = Math.max(position.y, maxY);
            maxZ = Math.max(position.z, maxZ);
            minX = Math.min(position.x, minX);
            minY = Math.min(position.y, minY);
            minZ = Math.min(position.z, minZ);
        }
        return new AABB(minX, minY, minZ, maxX, maxY, maxZ);
    }

    @Override
    public void submit(S entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(entityRenderState, poseStack, submitNodeCollector, cameraRenderState);
        submitTail(entityRenderState, poseStack, submitNodeCollector, entityRenderState.partialTranslation);
    }

    protected void submitTail(S renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 translation) {
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);
        poseStack.translate(translation);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            float partSize = 1 / (float) renderState.tailPositions.size();
            for (int index = 0; index < renderState.tailPositions.size() - 1; index++) {
                Vector3f from = renderState.tailPositions.get(index);
                Vector3f to = renderState.tailPositions.get(index + 1);
                float uv = index / (float) renderState.tailPositions.size();
                int alpha = NarakaRenderUtils.MAX_TAIL_ALPHA;
                if (uv > 0.5)
                    alpha = (int) (NarakaRenderUtils.MAX_TAIL_ALPHA * (1 - (uv - 0.5) * 2));
                renderTailPart(renderState, pose, vertexConsumer, from, to, uv, partSize, ARGB.color(alpha, renderState.tailColor));
            }
        });

        poseStack.popPose();
    }

    protected void renderTailPart(S renderState, PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3f from, Vector3f to, float index, float size, int color) {
        NarakaRenderUtils.renderTailPart(pose, vertexConsumer, from, to, renderState.tailWidth, index, size, color);
    }
}
