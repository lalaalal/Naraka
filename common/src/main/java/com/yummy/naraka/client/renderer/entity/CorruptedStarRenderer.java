package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.renderer.entity.state.CorruptedStarRenderState;
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
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.culling.Frustum;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.ARGB;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.Vec3;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public class CorruptedStarRenderer extends LightTailEntityRenderer<CorruptedStar, CorruptedStarRenderState> {
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
    public CorruptedStarRenderState createRenderState() {
        return new CorruptedStarRenderState();
    }

    @Override
    public void extractRenderState(CorruptedStar entity, CorruptedStarRenderState renderState, float partialTick) {
        super.extractRenderState(entity, renderState, partialTick);
        renderState.verticalShine = entity.isVerticalShine();
        renderState.shineLifetime = entity.getShineLifetime();
        renderState.shineScale = entity.getShineScale();
        renderState.shineStartTick = entity.getShineStartTick();
        renderState.shineRotation = entity.getShineRotation();
        renderState.targetPosition = entity.getTargetPosition(partialTick);
    }

    @Override
    public boolean shouldRender(CorruptedStar livingEntity, Frustum camera, double camX, double camY, double camZ) {
        return true;
    }

    @Override
    protected boolean affectedByCulling(CorruptedStar display) {
        return false;
    }

    @Override
    public void submit(CorruptedStarRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        float rotation = entityRenderState.ageInTicks * 10;
        poseStack.rotateAround(new Quaternionf().setAngleAxis(Mth.PI / 3, NarakaRenderUtils.SIN_45, 0, NarakaRenderUtils.SIN_45), 0, 0.25f, 0);
        poseStack.rotateAround(Axis.YP.rotationDegrees(rotation), 0, 0.25f, 0);
        poseStack.rotateAround(Axis.ZP.rotationDegrees(rotation), 0, 0.25f, 0);
        submitNodeCollector.order(0).submitModelPart(inner, poseStack, RenderTypes.lightning(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, 0xaaffffff, null);
        submitNodeCollector.order(1).submitModelPart(outer, poseStack, RenderTypes.lightning(), LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, null, ARGB.color(0xbb, SoulType.COPPER.color), null);

        poseStack.popPose();

        float tick = entityRenderState.ageInTicks - entityRenderState.shineStartTick;
        if (0 <= tick && tick <= entityRenderState.shineLifetime) {
            poseStack.pushPose();
            poseStack.translate(0, 0.25f, 0);
            ShinyEffectRenderer.submitShiny(tick, entityRenderState.shineLifetime, entityRenderState.shineScale, entityRenderState.verticalShine, SoulType.COPPER.color, poseStack, submitNodeCollector);
            poseStack.popPose();
        }
        submitTargetPoint(entityRenderState, poseStack, submitNodeCollector);

        super.submit(entityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    private void submitTargetPoint(CorruptedStarRenderState entityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector) {
        float tickPart = entityRenderState.ageInTicks - entityRenderState.shineStartTick;
        if (tickPart < 0 || entityRenderState.targetPosition.equals(Vec3.ZERO))
            return;

        poseStack.pushPose();
        Vec3 translation = entityRenderState.targetPosition.subtract(entityRenderState.x, entityRenderState.y, entityRenderState.z);
        poseStack.translate(translation.x, translation.y, translation.z);
        Player player = NarakaRenderUtils.getCurrentPlayer();

        poseStack.translate(0, 0.25f, 0);
        poseStack.mulPose(Axis.YN.rotationDegrees(player.getYRot() + 180));
        float width = NarakaUtils.interpolate(Math.min(tickPart / 10, 1), 0, 0.05f, NarakaUtils::fastStepOut);

        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            renderTargetPoint(pose, vertexConsumer, 0.25f, width, 1.5f, 0xab, SoulType.COPPER.color);
        });

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
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, x - (width), 0, z)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, x + (width), 0, z)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, -x, height, -z)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
    }
}
