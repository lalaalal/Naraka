package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.ShinyEffect;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.world.entity.player.Player;

@Environment(EnvType.CLIENT)
public class ShinyEffectRenderer extends EntityRenderer<ShinyEffect> {
    public ShinyEffectRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public boolean shouldRender(ShinyEffect livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    public void render(ShinyEffect entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float ageInTicks = entity.tickCount + partialTick;

        poseStack.pushPose();
        poseStack.mulPose(Axis.ZP.rotationDegrees(entity.getRotation()));
        submitShiny(ageInTicks, entity.getLifetime(), entity.getScale(), entity.isVertical(), entity.getColor(), poseStack, bufferSource);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(ShinyEffect entity) {
        return NarakaMod.location("empty");
    }


    public static void submitShiny(float tick, int lifetime, float scale, boolean isVertical, int color, PoseStack poseStack, MultiBufferSource bufferSource) {
        if (tick < 0 || tick > lifetime)
            return;

        poseStack.pushPose();
        poseStack.scale(scale, scale, scale);
        Player player = NarakaRenderUtils.getCurrentPlayer();

        poseStack.mulPose(Axis.YN.rotationDegrees(player.getYRot() + 180));
        if (isVertical)
            poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lightning());
        renderShiny(poseStack.last(), vertexConsumer, tick, lifetime, color);

        poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        renderShiny(poseStack.last(), vertexConsumer, tick, lifetime, color);
        poseStack.popPose();
    }

    private static void renderShiny(PoseStack.Pose pose, VertexConsumer vertexConsumer, float tick, int lifetime, int color) {
        float width = NarakaUtils.interpolate(tick / lifetime, 0, 20, NarakaUtils::fastStepIn);
        float height = NarakaUtils.interpolate(tick / lifetime, 0.1f, 0, NarakaUtils::fastStepOut);

        renderRhombus(pose, vertexConsumer, width, height, 0xff, 0xffffff);

        float centerWidth = Math.min(0.5f, width);
        float centerHeight = NarakaUtils.interpolate(tick / lifetime, 0.5f, 0, NarakaUtils::fastStepOut);
        int alpha = 0xff;
        while (centerWidth < width) {
            renderRhombus(pose, vertexConsumer, centerWidth, centerHeight, alpha, color);
            centerWidth *= 2;
            alpha = (int) (alpha * 0.75f);
            centerHeight += 0.05f;
        }
        renderRhombus(pose, vertexConsumer, width, centerHeight, 0x11, color);
    }

    private static void renderRhombus(PoseStack.Pose pose, VertexConsumer vertexConsumer, float width, float height, int alpha, int color) {
        vertexConsumer.addVertex(pose, 0, height, 0)
                .setNormal(pose, 0, 1, 0)
                .setColor(FastColor.ARGB32.color(alpha, color));
        vertexConsumer.addVertex(pose, -width, 0, 0)
                .setNormal(pose, 0, 1, 0)
                .setColor(FastColor.ARGB32.color(alpha, color));
        vertexConsumer.addVertex(pose, 0, -height, 0)
                .setNormal(pose, 0, 1, 0)
                .setColor(FastColor.ARGB32.color(alpha, color));
        vertexConsumer.addVertex(pose, width, 0, 0)
                .setNormal(pose, 0, 1, 0)
                .setColor(FastColor.ARGB32.color(alpha, color));
    }
}
