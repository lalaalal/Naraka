package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.MagicCircle;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class MagicCircleRenderer extends EntityRenderer<MagicCircle> {
    public MagicCircleRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(MagicCircle entity) {
        return NarakaTextures.MAGIC_CIRCLE;
    }

    @Override
    public void render(MagicCircle entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        float yRot = entity.getViewYRot(partialTick);
        float scale = entity.getScale(partialTick);
        poseStack.pushPose();
        poseStack.translate(0, 0.0125, 0);
        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.YN.rotation(yRot));
        RenderType renderType = RenderType.entityCutout(getTextureLocation(entity));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        NarakaRenderUtils.renderFlatImage(poseStack.last(), vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1, Direction.Axis.Y);
        poseStack.popPose();
    }
}
