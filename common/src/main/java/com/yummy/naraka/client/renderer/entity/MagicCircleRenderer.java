package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.MagicCircleRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.MagicCircle;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import org.joml.Vector3f;

import java.util.List;

public class MagicCircleRenderer extends EntityRenderer<MagicCircle, MagicCircleRenderState> {
    public MagicCircleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public MagicCircleRenderState createRenderState() {
        return new MagicCircleRenderState();
    }

    @Override
    public void extractRenderState(MagicCircle entity, MagicCircleRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.yRot = entity.getYRot(partialTick);
        reusedState.scale = entity.getScale();
    }

    @Override
    public void render(MagicCircleRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotation(renderState.yRot));
        poseStack.scale(renderState.scale, renderState.scale, renderState.scale);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.MAGIC_CIRCLE));
        List<Vector3f> vertices = List.of(
                new Vector3f(-0.5f, 0, 0.5f),
                new Vector3f(-0.5f, 0, -0.5f),
                new Vector3f(0.5f, 0, -0.5f),
                new Vector3f(0.5f, 0, 0.5f)
        );
        NarakaRenderUtils.vertices(vertexConsumer, poseStack.last(), vertices, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.UP);
        NarakaRenderUtils.vertices(vertexConsumer, poseStack.last(), vertices, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.DOWN);
        poseStack.popPose();
        super.render(renderState, poseStack, bufferSource, packedLight);
    }
}
