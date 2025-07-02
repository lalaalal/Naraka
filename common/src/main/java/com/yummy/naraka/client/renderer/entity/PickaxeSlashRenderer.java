package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.LightTailEntityRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.PickaxeSlash;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.util.ARGB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.function.BiFunction;

@Environment(EnvType.CLIENT)
public class PickaxeSlashRenderer extends LightTailEntityRenderer<PickaxeSlash, LightTailEntityRenderState> {
    public PickaxeSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public LightTailEntityRenderState createRenderState() {
        return new LightTailEntityRenderState();
    }

    @Override
    public void extractRenderState(PickaxeSlash entity, LightTailEntityRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.yRot = 180 + entity.getYRot(partialTick);
        reusedState.zRot = entity.getZRot();
        reusedState.tailWidth = 1;
    }

    @Override
    public void render(LightTailEntityRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(renderState, poseStack, bufferSource, packedLight);
        poseStack.pushPose();
        poseStack.scale(6, 6, 6);
        poseStack.translate(0, -0.25, 0);
        poseStack.mulPose(Axis.YN.rotationDegrees(renderState.yRot));
        poseStack.rotateAround(Axis.ZN.rotationDegrees(renderState.zRot), 0, 0.5f, 0);
        poseStack.translate(0, 0, -0.25);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.PICKAXE_SLASH));
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1, Direction.Axis.X);
        poseStack.popPose();
    }

    @Override
    protected void renderTail(LightTailEntityRenderState renderState, PoseStack poseStack, MultiBufferSource buffer, Vec3 translation) {
        Vector3f vector3f = new Vector3f(0, 0, 1)
                .rotate(Axis.ZN.rotationDegrees(renderState.zRot))
                .rotate(Axis.YN.rotationDegrees(renderState.yRot));
        super.renderTail(renderState, poseStack, buffer, new Vec3(vector3f).add(translation).add(0, 1.2, 0));
    }

    @Override
    protected void renderTailPart(LightTailEntityRenderState renderState, PoseStack poseStack, VertexConsumer vertexConsumer, Vector3f from, Vector3f to, float index, float size, int color) {
        int alpha = ARGB.alpha(color);
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer,
                NarakaRenderUtils.createVertices(from, to, 1.5f, modifier(renderState.yRot, renderState.zRot)), index, index, size, size,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ARGB.color(alpha / 4, color)
        );
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer,
                NarakaRenderUtils.createVertices(from, to, 1, modifier(renderState.yRot, renderState.zRot)), index, index, size, size,
                LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, ARGB.color(alpha / 2, color)
        );
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer,
                NarakaRenderUtils.createVertices(from, to, 0.5f, modifier(renderState.yRot, renderState.zRot)), index, index, size, size,
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
