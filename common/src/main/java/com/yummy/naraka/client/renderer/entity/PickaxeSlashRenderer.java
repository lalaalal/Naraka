package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.entity.state.FlatImageRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.PickaxeSlash;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;

@Environment(EnvType.CLIENT)
public class PickaxeSlashRenderer extends EntityRenderer<PickaxeSlash, FlatImageRenderState> {
    public PickaxeSlashRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public FlatImageRenderState createRenderState() {
        return new FlatImageRenderState();
    }

    @Override
    public void extractRenderState(PickaxeSlash entity, FlatImageRenderState reusedState, float partialTick) {
        super.extractRenderState(entity, reusedState, partialTick);
        reusedState.yRot = 180 + entity.getYRot(partialTick);
    }

    @Override
    public void render(FlatImageRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();
        poseStack.translate(0, 0, 0.5);
        poseStack.scale(3, 3, 1.26f * 3);
        poseStack.mulPose(Axis.YN.rotationDegrees(renderState.yRot));
        poseStack.rotateAround(Axis.ZN.rotationDegrees(30), 0, 0.5f, 0);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityTranslucentEmissive(NarakaTextures.PICKAXE_SLASH));
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1, Direction.EAST);
        poseStack.popPose();
        super.render(renderState, poseStack, bufferSource, packedLight);
    }
}
