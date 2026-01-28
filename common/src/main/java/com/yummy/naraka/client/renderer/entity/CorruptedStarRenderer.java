package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.NarakaMod;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.util.NarakaUtils;
import com.yummy.naraka.world.entity.CorruptedStar;
import com.yummy.naraka.world.item.SoulType;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class CorruptedStarRenderer extends LightTailEntityRenderer<CorruptedStar> {
    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition root = meshdefinition.getRoot();

        root.addOrReplaceChild("inner", CubeListBuilder.create()
                        .addBox(-3.0F, -3.0F, -3.0F, 6.0F, 6.0F, 6.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 4.0F, 0.0F)
        );
        root.addOrReplaceChild("outer", CubeListBuilder.create()
                        .addBox(-4.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 4.0F, 0.0F)
        );

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    private final ModelPart outer;
    private final ModelPart inner;

    public CorruptedStarRenderer(EntityRendererProvider.Context context) {
        super(context);
        ModelPart root = context.bakeLayer(NarakaModelLayers.CORRUPTED_STAR);
        this.outer = root.getChild("outer");
        this.inner = root.getChild("inner");
    }

    @Override
    public void render(CorruptedStar entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        float ageInTicks = entity.tickCount + partialTick;
        poseStack.pushPose();
        float rotation = ageInTicks * 10;
        poseStack.rotateAround(new Quaternionf().setAngleAxis(Mth.PI / 3, NarakaRenderUtils.SIN_45, 0, NarakaRenderUtils.SIN_45), 0, 0.25f, 0);
        poseStack.rotateAround(Axis.YP.rotationDegrees(rotation), 0, 0.25f, 0);
        poseStack.rotateAround(Axis.ZP.rotationDegrees(rotation), 0, 0.25f, 0);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.lightning());
        inner.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xaaffffff);
        outer.render(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, FastColor.ARGB32.color(0xbb, SoulType.COPPER.color));

        poseStack.popPose();

        float tick = entity.tickCount - entity.getShineStartTick();
        if (0 <= tick && tick <= entity.getShineLifetime()) {
            poseStack.pushPose();
            poseStack.translate(0, 0.25f, 0);
            ShinyEffectRenderer.submitShiny(tick, entity.getShineLifetime(), entity.getShineScale(), entity.isVerticalShine(), SoulType.COPPER.color, poseStack, bufferSource);
            poseStack.popPose();
        }
        submitTargetPoint(entity, poseStack, partialTick, vertexConsumer);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    public ResourceLocation getTextureLocation(CorruptedStar entity) {
        return NarakaMod.location("empty");
    }

    private void submitTargetPoint(CorruptedStar entity, PoseStack poseStack, float partialTick, VertexConsumer vertexConsumer) {
        float ageInTicks = entity.tickCount + partialTick;
        float tickPart = ageInTicks - entity.getShineStartTick();
        Vec3 targetPosition = entity.getTargetPosition(partialTick);
        if (tickPart < 0 || targetPosition.equals(Vec3.ZERO))
            return;

        poseStack.pushPose();
        Vec3 position = entity.getPosition(partialTick);
        Vec3 translation = targetPosition.subtract(position.x, position.y, position.z);
        poseStack.translate(translation.x, translation.y, translation.z);
        Player player = NarakaRenderUtils.getCurrentPlayer();

        poseStack.translate(0, 0.25f, 0);
        poseStack.mulPose(Axis.YN.rotationDegrees(player.getYRot() + 180));
        float width = NarakaUtils.interpolate(Math.min(tickPart / 10, 1), 0, 0.05f, NarakaUtils::fastStepOut);

        renderTargetPoint(poseStack.last(), vertexConsumer, 0.25f, width, 1.5f, 0xab, SoulType.COPPER.color);

        poseStack.popPose();
    }

    private void renderTargetPoint(PoseStack.Pose pose, VertexConsumer vertexConsumer, float interval, float width, float height, int alpha, int color) {
        renderTriangle(pose, vertexConsumer, 1, 1, interval, width, height, alpha, color);
        renderTriangle(pose, vertexConsumer, -1, 1, interval, width, height, alpha, color);
        renderTriangle(pose, vertexConsumer, -1, -1, interval, width, height, alpha, color);
        renderTriangle(pose, vertexConsumer, 1, -1, interval, width, height, alpha, color);
    }

    private void renderTriangle(PoseStack.Pose pose, VertexConsumer vertexConsumer, int xDirection, int zDirection, float interval, float width, float height, int alpha, int color) {
        float x = interval * xDirection;
        float z = interval * zDirection;
        vertexConsumer.addVertex(pose, -x, height, -z)
                .setNormal(pose, 0, 1, 0)
                .setColor(FastColor.ARGB32.color(alpha, color));
        vertexConsumer.addVertex(pose, x - (width), 0, z)
                .setNormal(pose, 0, 1, 0)
                .setColor(FastColor.ARGB32.color(alpha, color));
        vertexConsumer.addVertex(pose, x + (width), 0, z)
                .setNormal(pose, 0, 1, 0)
                .setColor(FastColor.ARGB32.color(alpha, color));
        vertexConsumer.addVertex(pose, -x, height, -z)
                .setNormal(pose, 0, 1, 0)
                .setColor(FastColor.ARGB32.color(alpha, color));
    }
}
