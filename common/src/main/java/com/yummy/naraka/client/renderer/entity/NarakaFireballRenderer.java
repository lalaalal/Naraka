package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.model.NarakaFireballModel;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.NarakaFireball;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class NarakaFireballRenderer extends EntityRenderer<NarakaFireball> {
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
        float ageInTicks = entity.tickCount + partialTick;
        float rotation = ageInTicks * 5;
        poseStack.translate(0, 0.33, 0);
        NarakaRenderUtils.applyYZSpin(poseStack, rotation);
        RenderType renderType = model.renderType(getTextureLocation(entity));
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        model.renderToBuffer(poseStack, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
        poseStack.popPose();
    }
}
