package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.MagicCircleRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.MagicCircle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import org.joml.Quaternionfc;
import org.joml.Vector3f;
import org.joml.Vector3fc;

import java.util.List;

@Environment(EnvType.CLIENT)
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
        reusedState.scale = entity.getScale(partialTick);
    }

    @Override
    public void render(MagicCircleRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        List<Quaternionfc> rotations = List.of(Axis.YN.rotation(renderState.yRot));
        Vector3fc scale = new Vector3f(renderState.scale);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.MAGIC_CIRCLE));
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer, rotations, scale, packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.UP);
        super.render(renderState, poseStack, bufferSource, packedLight);
    }
}
