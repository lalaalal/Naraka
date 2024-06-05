package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.model.SpearOfLonginusModel;
import com.yummy.naraka.entity.SpearOfLonginus;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.TheEndPortalRenderer;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider.Context;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class SpearOfLonginusRenderer extends EntityRenderer<SpearOfLonginus> {
    private final SpearOfLonginusModel model;

    public SpearOfLonginusRenderer(Context context) {
        super(context);
        this.model = new SpearOfLonginusModel(context.bakeLayer(NarakaModelLayers.SPEAR_OF_LONGINUS));
    }

    @Override
    public ResourceLocation getTextureLocation(SpearOfLonginus spearOfLonginus) {
        return TheEndPortalRenderer.END_PORTAL_LOCATION;
    }

    @Override
    public void render(SpearOfLonginus spearOfLonginus, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        RenderType renderType = model.renderType(getTextureLocation(spearOfLonginus));
        VertexConsumer vertexConsumer = buffer.getBuffer(renderType);
        model.renderToBuffer(poseStack, vertexConsumer, packedLight, OverlayTexture.WHITE_OVERLAY_V, 1, 1, 1, 1);
    }
}