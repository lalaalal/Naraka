package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.NarakaFireballModel;
import com.yummy.naraka.world.entity.NarakaFireball;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Quaternionf;

public class NarakaFireballRenderer extends EntityRenderer<NarakaFireball> {
    private static final float SIN_45 = (float) Math.sin(Math.PI / 4);

    private final NarakaFireballModel model;

    public NarakaFireballRenderer(EntityRendererProvider.Context context) {
        super(context);
        this.model = new NarakaFireballModel(context.bakeLayer(NarakaModelLayers.NARAKA_FIREBALL));
    }

    @Override
    public ResourceLocation getTextureLocation(NarakaFireball entity) {
        return NarakaTextures.NARAKA_FIREBALL;
    }

    @Override
    public void render(NarakaFireball entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        poseStack.pushPose();

        float rotation = (entity.tickCount + partialTick) * 5;
        poseStack.scale(1.25f, 1.25f, 1.25f);
        poseStack.translate(0, 0.375f, 0);
        poseStack.mulPose(new Quaternionf().setAngleAxis((float) (Math.PI / 3), SIN_45, 0.0F, SIN_45));
        poseStack.mulPose(Axis.YP.rotationDegrees(rotation));
        poseStack.mulPose(Axis.ZP.rotationDegrees(rotation * 2));
        poseStack.translate(0, -1.5, 0);

        RenderType renderType = model.renderType(getTextureLocation(entity));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);

        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.NO_OVERLAY);
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);

        poseStack.popPose();
    }
}
