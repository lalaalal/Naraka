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
        renderState.shineScale = entity.getShineScale();
        renderState.shineStartTick = entity.getShineStartTick();
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

        poseStack.pushPose();
        poseStack.scale(entityRenderState.shineScale, entityRenderState.shineScale, entityRenderState.shineScale);
        Player player = NarakaRenderUtils.getCurrentPlayer();

        poseStack.translate(0, 0.25f, 0);
        poseStack.mulPose(Axis.YN.rotationDegrees(player.getYRot() + 180));
        if (entityRenderState.verticalShine)
            poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            renderShiny(pose, vertexConsumer, entityRenderState);
        });

        poseStack.mulPose(Axis.ZN.rotationDegrees(90));
        poseStack.scale(0.5f, 0.5f, 0.5f);
        submitNodeCollector.submitCustomGeometry(poseStack, RenderTypes.lightning(), (pose, vertexConsumer) -> {
            renderShiny(pose, vertexConsumer, entityRenderState);
        });
        poseStack.popPose();

        super.submit(entityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

    private void renderShiny(PoseStack.Pose pose, VertexConsumer vertexConsumer, CorruptedStarRenderState entityRenderState) {
        int length = 20;

        float tickPart = entityRenderState.ageInTicks - entityRenderState.shineStartTick;
        if (tickPart < 0 || tickPart > length)
            return;

        float width = NarakaUtils.interpolate(tickPart / length, 0, 20, NarakaUtils::fastStepIn);
        float height = NarakaUtils.interpolate(tickPart / length, 0.1f, 0, NarakaUtils::fastStepOut);

        renderRhombus(pose, vertexConsumer, width, height, 0xff, 0xffffff);

        float centerWidth = Math.min(0.5f, width);
        float centerHeight = NarakaUtils.interpolate(tickPart / length, 0.5f, 0, NarakaUtils::fastStepOut);
        int alpha = 0xff;
        while (centerWidth < width) {
            renderRhombus(pose, vertexConsumer, centerWidth, centerHeight, alpha, SoulType.COPPER.color);
            centerWidth *= 2;
            alpha = (int) (alpha * 0.75f);
            centerHeight += 0.05f;
        }
        renderRhombus(pose, vertexConsumer, width, centerHeight, 0x11, SoulType.COPPER.color);
    }

    private void renderRhombus(PoseStack.Pose pose, VertexConsumer vertexConsumer, float width, float height, int alpha, int color) {
        vertexConsumer.addVertex(pose, 0, height, 0)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, -width, 0, 0)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, 0, -height, 0)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
        vertexConsumer.addVertex(pose, width, 0, 0)
                .setNormal(pose, 0, 1, 0)
                .setColor(ARGB.color(alpha, color));
    }
}
