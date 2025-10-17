package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.LightTailEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.util.FastColor;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;

@Environment(EnvType.CLIENT)
public abstract class LightTailEntityRenderer<T extends LightTailEntity> extends EntityRenderer<T> {
    private static final float TAIL_WIDTH = 0.15f;

    protected LightTailEntityRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(T livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        Vec3 partialTranslation = entity.position()
                .subtract(entity.getPosition(partialTick));
        renderTail(entity, partialTick, poseStack, bufferSource, partialTranslation);
    }

    protected void renderTail(T entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, Vec3 translation) {
        poseStack.pushPose();
        poseStack.translate(0, 0.25, 0);
        poseStack.translate(translation.x, translation.y, translation.z);
        List<Vector3f> tailPositions = entity.getTailPositions().stream()
                .map(position -> position.subtract(entity.position()))
                .map(NarakaRenderUtils::vector3f)
                .toList();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lightning());
        float partSize = 1 / (float) tailPositions.size();
        for (int index = 0; index < tailPositions.size() - 1; index++) {
            Vector3f from = tailPositions.get(index);
            Vector3f to = tailPositions.get(index + 1);
            float uv = index / (float) tailPositions.size();
            int alpha = NarakaRenderUtils.MAX_TAIL_ALPHA;
            if (uv > 0.5)
                alpha = (int) (NarakaRenderUtils.MAX_TAIL_ALPHA * (1 - (uv - 0.5) * 2));
            renderTailPart(entity, partialTick, poseStack.last(), vertexConsumer, from, to, uv, partSize, FastColor.ARGB32.color(alpha, entity.getTailColor()));
        }

        poseStack.popPose();
    }

    protected void renderTailPart(T entity, float partialTick, PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3f from, Vector3f to, float index, float size, int color) {
        NarakaRenderUtils.renderTailPart(pose, vertexConsumer, from, to, TAIL_WIDTH, index, size, color);
    }
}
