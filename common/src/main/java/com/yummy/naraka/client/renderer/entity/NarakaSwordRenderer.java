package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.renderer.entity.state.NarakaSwordRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.NarakaSword;
import com.yummy.naraka.world.entity.SwordEffectData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NarakaSwordRenderer extends EntityRenderer<NarakaSword, NarakaSwordRenderState> {
    public NarakaSwordRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    protected boolean affectedByCulling(NarakaSword display) {
        return false;
    }

    @Override
    public boolean shouldRender(NarakaSword livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public NarakaSwordRenderState createRenderState() {
        return new NarakaSwordRenderState();
    }

    @Override
    public void extractRenderState(NarakaSword entity, NarakaSwordRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.color = entity.getColor();
        renderState.maxAlpha = entity.getAlpha(partialTick);
        renderState.rotation = entity.getRotation(partialTick);
        renderState.scale = entity.getScale();

        Vector3fc position = entity.getPosition(partialTick).toVector3f();
        renderState.swordEffectOffset = position.mul(-1, new Vector3f());
        renderState.swordEffectUpdateCount = entity.getSwordEffectUpdateCount();

        renderState.swordEffectData = new ArrayList<>(entity.getSwordEffectData(partialTick));
        renderState.swordEffectData.addFirst(new SwordEffectData(position, NarakaSword.DIRECTION, renderState.rotation, NarakaSword.LENGTH, entity.getScale()));

        float alpha = 1;
        for (float index = 0; index < renderState.swordEffectData.size() - 1; index++) {
            float nextAlpha = alpha * 0.95f;
            renderState.swordEffectAlpha.add(Mth.lerp(partialTick, alpha, nextAlpha));

            alpha = nextAlpha;
        }
    }

    @Override
    public void submit(NarakaSwordRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        if (entityRenderState.maxAlpha <= 0.01f)
            return;

        poseStack.pushPose();
        submitNodeCollector.order(1).submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            renderSwordEffect(pose, vertexConsumer, entityRenderState);
        });

        poseStack.scale(entityRenderState.scale, entityRenderState.scale, entityRenderState.scale);
        ShinyEffectRenderer.submitShiny(entityRenderState.maxAlpha * 50, 100, 0.125f, false, entityRenderState.color, poseStack, submitNodeCollector);

        poseStack.mulPose(entityRenderState.rotation);

        submitNodeCollector.order(0).submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            renderBody(pose, vertexConsumer, -0.2f, 0.2f, 3, 0.25f, 0.67f * entityRenderState.maxAlpha, entityRenderState.color);
            renderBody(pose, vertexConsumer, 0.1f, 0.2f, 3, 0.0625f, entityRenderState.maxAlpha, entityRenderState.color);
            renderHandle(pose, vertexConsumer, 0, 0, -1.5f, 0.5f, 0.15f, 0.15f, 1.5f, 0x67f * entityRenderState.maxAlpha, entityRenderState.color);
            renderHandle(pose, vertexConsumer, Mth.PI, 0, -1.5f, 0.5f, 0.15f, 0.15f, 1.5f, 0x67f * entityRenderState.maxAlpha, entityRenderState.color);
        });
        poseStack.popPose();

        super.submit(entityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    private void renderBody(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x1, float x2, float height, float headCut, float alpha, int color) {
        List<Vector3fc> vertices = List.of(
                new Vector3f(x1, height - headCut, 0),
                new Vector3f(x1, 0, 0),
                new Vector3f(x2, 0, 0),
                new Vector3f(x2, height, 0)
        );
        NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, vertices, 0, 0, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ARGB.color(alpha, color));
    }

    private void renderHandle(PoseStack.Pose pose, VertexConsumer vertexConsumer, float angleOffset, float xOffset, float yOffset, float handleHeight, float height, float scale, float length, float alpha, int color) {
        final float delta = Mth.PI * 0.01f;
        float prevX = Mth.cos(angleOffset) * scale + xOffset;
        float prevZ = Mth.sin(angleOffset) * scale;
        float prevY = yOffset;

        for (float angle = delta; angle < Mth.PI * 2 * length; angle += delta) {
            float x = Mth.cos(angle + angleOffset) * scale + xOffset;
            float z = Mth.sin(angle + angleOffset) * scale;
            float y = angle / Mth.PI * handleHeight + yOffset;

            renderWithHeight(pose, vertexConsumer, prevX, prevY, prevZ, x, y, z, height, ARGB.color(alpha, color));

            prevX = x;
            prevZ = z;
            prevY = y;
        }
    }

    private void renderWithHeight(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float height, int color) {
        List<Vector3fc> vertices = List.of(
                new Vector3f(x1, y1 + height, z1),
                new Vector3f(x1, y1, z1),
                new Vector3f(x2, y2, z2),
                new Vector3f(x2, y2 + height, z2)
        );
        NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, vertices, 0, 0, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color);
    }

    private void renderSwordEffect(PoseStack.Pose pose, VertexConsumer vertexConsumer, NarakaSwordRenderState renderState) {
        List<SwordEffectData> swordEffectData = renderState.swordEffectData;
        for (int index = 0; index < renderState.swordEffectAlpha.size() - 1; index++) {
            float alpha = renderState.swordEffectAlpha.get(index);
            SwordEffectData current = swordEffectData.get(index);
            SwordEffectData next = swordEffectData.get(index + 1);

            if (alpha < 0.01f || next.length() <= 0)
                return;

            List<Vector3fc> vertices = List.of(
                    current.head(renderState.swordEffectOffset),
                    current.tail(renderState.swordEffectOffset),
                    next.tail(renderState.swordEffectOffset),
                    next.head(renderState.swordEffectOffset)
            );
            NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, vertices, 0, 0, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ARGB.color(alpha, renderState.color));
        }
    }
}
