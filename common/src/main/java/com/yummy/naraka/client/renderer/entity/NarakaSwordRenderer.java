package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.NarakaSword;
import com.yummy.naraka.world.entity.SwordEffectData;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.ArrayList;
import java.util.List;

@Environment(EnvType.CLIENT)
public class NarakaSwordRenderer extends EntityRenderer<NarakaSword> {
    public NarakaSwordRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(NarakaSword livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    private float calculateAlpha(float delta) {
        delta = Mth.clamp(delta, 0, 1);
        return Mth.lerp(delta, 1, 0);
    }

    @Override
    public void render(NarakaSword entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float maxAlpha = entity.getAlpha(partialTick);
        int color = entity.getColor();
        float scale = entity.getScale();
        Quaternionf rotation = entity.getRotation(partialTick);

        if (maxAlpha <= 0.01f)
            return;

        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lightning());

        renderSwordEffect(poseStack.last(), vertexConsumer, partialTick, entity);

        poseStack.scale(scale, scale, scale);
        ShinyEffectRenderer.renderShiny(maxAlpha * 50, 100, 0.125f, false, color, poseStack, bufferSource);

        poseStack.mulPose(rotation);
        renderSword(entity, poseStack, bufferSource, partialTick);
        poseStack.popPose();

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(NarakaSword entity) {
        return NarakaTextures.AREA_EFFECT;
    }

    private void renderSword(NarakaSword narakaSword, PoseStack poseStack, MultiBufferSource bufferSource, float partialTick) {
        int color = narakaSword.getColor();
        float maxAlpha = narakaSword.getAlpha(partialTick);

        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lightning());
        PoseStack.Pose pose = poseStack.last();
        renderBody(pose, vertexConsumer, -0.2f, 0.2f, 3, 0.25f, 0.67f * maxAlpha, color);
        renderBody(pose, vertexConsumer, 0.1f, 0.2f, 3, 0.0625f, maxAlpha, color);
        renderHandle(pose, vertexConsumer, 0, 0, -1.5f, 0.5f, 0.15f, 0.15f, 1.5f, 0x67f * maxAlpha, color);
        renderHandle(pose, vertexConsumer, Mth.PI, 0, -1.5f, 0.5f, 0.15f, 0.15f, 1.5f, 0x67f * maxAlpha, color);

        renderBody(pose, vertexConsumer, -0.2f, 0.2f, 3, 0.25f, 0.67f * maxAlpha, color);
        renderBody(pose, vertexConsumer, 0.1f, 0.2f, 3, 0.0625f, maxAlpha, color);
    }

    private void renderBody(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x1, float x2, float height, float headCut, float alpha, int color) {
        List<Vector3f> vertices = List.of(
                new Vector3f(x1, height - headCut, 0),
                new Vector3f(x1, 0, 0),
                new Vector3f(x2, 0, 0),
                new Vector3f(x2, height, 0)
        );
        NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, vertices, 0, 0, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int) (alpha * 255), color));
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

            renderWithHeight(pose, vertexConsumer, prevX, prevY, prevZ, x, y, z, height, FastColor.ARGB32.color((int) (alpha * 255), color));

            prevX = x;
            prevZ = z;
            prevY = y;
        }
    }

    private void renderWithHeight(PoseStack.Pose pose, VertexConsumer vertexConsumer, float x1, float y1, float z1, float x2, float y2, float z2, float height, int color) {
        List<Vector3f> vertices = List.of(
                new Vector3f(x1, y1 + height, z1),
                new Vector3f(x1, y1, z1),
                new Vector3f(x2, y2, z2),
                new Vector3f(x2, y2 + height, z2)
        );
        NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, vertices, 0, 0, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color);
    }

    private void renderSwordEffect(PoseStack.Pose pose, VertexConsumer vertexConsumer, float partialTick, NarakaSword narakaSword) {
        List<SwordEffectData> swordEffectData = narakaSword.getSwordEffectData(partialTick);
        List<Float> swordEffectAlpha = new ArrayList<>();
        for (float index = 0; index < swordEffectData.size(); index++) {
            float currentAlpha = calculateAlpha(index / swordEffectData.size());
            float nextAlpha = calculateAlpha((index + narakaSword.getSwordEffectUpdateCount()) / swordEffectData.size());
            swordEffectAlpha.add(Mth.lerp(partialTick, currentAlpha, nextAlpha));
        }
        Vector3fc position = narakaSword.getPosition(partialTick).toVector3f();
        Vector3fc swordEffectOffset = position.mul(-1, new Vector3f());

        for (int index = 0; index < swordEffectAlpha.size() - 1; index++) {
            float alpha = swordEffectAlpha.get(index);
            SwordEffectData current = swordEffectData.get(index);
            SwordEffectData next = swordEffectData.get(index + 1);

            if (alpha < 0.01f || next.length() <= 0)
                return;

            List<Vector3f> vertices = List.of(
                    current.head(swordEffectOffset),
                    current.tail(swordEffectOffset),
                    next.tail(swordEffectOffset),
                    next.head(swordEffectOffset)
            );
            NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, vertices, 0, 0, 1, 1, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color((int) (alpha * 255), narakaSword.getColor()));
        }
    }
}
