package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
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
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.stream.Collectors;

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
        renderState.yRot = entity.getYRot(partialTick);
        renderState.zRot = entity.getZRot(partialTick);
        Vec3 offset = entity.getPosition(partialTick).scale(-1);
        renderState.swordEffectData = entity.getSwordEffectData()
                .stream()
                .map(swordEffectData -> swordEffectData.offset(offset))
                .collect(Collectors.toList());
        renderState.swordEffectData.addFirst(SwordEffectData.of(Vec3.ZERO, NarakaSword.DIRECTION, NarakaSword.LENGTH, renderState.zRot, renderState.yRot));
    }

    @Override
    public void submit(NarakaSwordRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        ShinyEffectRenderer.submitShiny(1, 2, 0.125f, false, entityRenderState.color, poseStack, submitNodeCollector);

        submitNodeCollector.order(0).submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            renderSwordEffect(pose, vertexConsumer, entityRenderState);
        });

        poseStack.mulPose(Axis.YP.rotationDegrees(entityRenderState.yRot));
        poseStack.mulPose(Axis.ZN.rotationDegrees(entityRenderState.zRot));
        poseStack.mulPose(Axis.YP.rotationDegrees(90));

        submitNodeCollector.order(1).submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(NarakaTextures.AREA_EFFECT), (pose, vertexConsumer) -> {
            renderBody(pose, vertexConsumer, -0.2f, 0.2f, 3, 0.25f, 0xaa, entityRenderState.color);
            renderBody(pose, vertexConsumer, 0.1f, 0.2f, 3, 0.0625f, 0xff, entityRenderState.color);
            renderHandle(pose, vertexConsumer, 0, 0, -1.5f, 0.5f, 0.15f, 0.15f, 1.5f, ARGB.color(0xab, entityRenderState.color));
            renderHandle(pose, vertexConsumer, Mth.PI, 0, -1.5f, 0.5f, 0.15f, 0.15f, 1.5f, ARGB.color(0xab, entityRenderState.color));
        });
        poseStack.popPose();

        poseStack.pushPose();
        poseStack.translate(0, -0.25f, 0);
        super.submit(entityRenderState, poseStack, submitNodeCollector, cameraRenderState);
        poseStack.popPose();
    }

    private void renderBody(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x1, float x2, float height, float headCut, int alpha, int color) {
        vertexConsumer.addVertex(pose, x1, height - headCut, 0)
                .setUv(0, 1)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, x1, 0, 0)
                .setUv(0, 0)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, x2, 0, 0)
                .setUv(1, 0)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, x2, height, 0)
                .setUv(1, 1)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
    }

    private void renderHandle(PoseStack.Pose pose, VertexConsumer vertexConsumer, float angleOffset, float xOffset, float yOffset, float handleHeight, float height, float scale, float length, int color) {
        final float delta = Mth.PI * 0.01f;
        float prevX = Mth.cos(angleOffset) * scale + xOffset;
        float prevZ = Mth.sin(angleOffset) * scale;
        float prevY = yOffset;

        for (float angle = delta; angle < Mth.PI * 2 * length; angle += delta) {
            float x = Mth.cos(angle + angleOffset) * scale + xOffset;
            float z = Mth.sin(angle + angleOffset) * scale;
            float y = angle / Mth.PI * handleHeight + yOffset;

            renderWithHeight(pose, vertexConsumer, prevX, prevY, prevZ, x, y, z, height, color);

            prevX = x;
            prevZ = z;
            prevY = y;
        }
    }

    private void renderWithHeight(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float height, int color) {
        vertexConsumer.addVertex(pose, x1, y1 + height, z1)
                .setUv(0, 1)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(pose, 0, 1, 0)
                .setColor(color);
        vertexConsumer.addVertex(pose, x1, y1, z1)
                .setUv(0, 0)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(pose, 0, 1, 0)
                .setColor(color);
        vertexConsumer.addVertex(pose, x2, y2, z2)
                .setUv(1, 0)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(pose, 0, 1, 0)
                .setColor(color);
        vertexConsumer.addVertex(pose, x2, y2 + height, z2)
                .setUv(1, 1)
                .setLight(LightTexture.FULL_BRIGHT)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setNormal(pose, 0, 1, 0)
                .setColor(color);
    }

    private void renderSwordEffect(PoseStack.Pose pose, VertexConsumer vertexConsumer, NarakaSwordRenderState renderState) {
        List<SwordEffectData> swordEffectData = renderState.swordEffectData;
        float alpha = 1;
        for (int index = 0; index < swordEffectData.size() - 1; index++) {
            SwordEffectData current = swordEffectData.get(index);
            SwordEffectData next = swordEffectData.get(index + 1);

            List<Vector3f> vertices = List.of(
                    current.head().toVector3f(),
                    current.tail().toVector3f(),
                    next.tail().toVector3f(),
                    next.head().toVector3f()
            );
            NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, vertices, 0, 0, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ARGB.color(alpha, renderState.color));

            alpha *= 0.9f;
        }
    }
}
