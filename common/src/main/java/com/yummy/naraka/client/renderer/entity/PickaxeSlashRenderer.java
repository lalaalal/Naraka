package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.PickaxeSlashRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.PickaxeSlash;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class PickaxeSlashRenderer extends LightTailEntityRenderer<PickaxeSlash, PickaxeSlashRenderState> {
    public PickaxeSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public PickaxeSlashRenderState createRenderState() {
        return new PickaxeSlashRenderState();
    }

    @Override
    public void extractRenderState(PickaxeSlash entity, PickaxeSlashRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.yRot = 180 + entity.getYRot(partialTick);
        reusedState.zRot = entity.getZRot();
        reusedState.tailWidth = 1;
        reusedState.color = entity.getColor(partialTick);
    }

    @Override
    public void submit(PickaxeSlashRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState) {
        super.submit(renderState, poseStack, submitNodeCollector, cameraRenderState);
        poseStack.pushPose();
        poseStack.scale(6, 6, 6);
        poseStack.translate(0, -0.25, 0);
        poseStack.mulPose(Axis.YN.rotationDegrees(renderState.yRot));
        poseStack.rotateAround(Axis.ZN.rotationDegrees(renderState.zRot), 0, 0.5f, 0);
        poseStack.translate(0, 0, -0.25);
        submitNodeCollector.order(1).submitCustomGeometry(poseStack, RenderTypes.entityTranslucent(NarakaTextures.PICKAXE_SLASH), (pose, vertexConsumer) -> {
            NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, renderState.color, Direction.Axis.X);
        });
        poseStack.popPose();
    }

    @Override
    protected void submitTail(PickaxeSlashRenderState renderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, Vec3 translation) {
        Vector3f vector3f = new Vector3f(0, 0, 1)
                .rotate(Axis.ZN.rotationDegrees(renderState.zRot))
                .rotate(Axis.YN.rotationDegrees(renderState.yRot));
        super.submitTail(renderState, poseStack, submitNodeCollector, new Vec3(vector3f).add(translation).add(0, 1.2, 0));
    }

    @Override
    protected void renderTailPart(PickaxeSlashRenderState renderState, PoseStack.Pose pose, VertexConsumer vertexConsumer, Vector3f from, Vector3f to, float index, float size, int color) {
        NarakaRenderUtils.renderFlatImage(pose, vertexConsumer,
                NarakaRenderUtils.createVertices(from, to, 1.5f, modifier(renderState.yRot, renderState.zRot)), index, index, size, size,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, color
        );
    }

    protected BiFunction<Vector3f, Float, Vector3f> modifier(float yRot, float zRot) {
        return (vector, interval) -> {
            Vector3f result = new Vector3f(0, interval, 0)
                    .rotate(Axis.ZN.rotationDegrees(zRot))
                    .rotate(Axis.YN.rotationDegrees(yRot));
            return result.add(vector);
        };
    }
}
