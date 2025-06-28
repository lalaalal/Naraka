package com.yummy.naraka.client.renderer.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.entity.SwordAura;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;

import java.util.List;

@Environment(EnvType.CLIENT)
public class SwordAuraRenderer extends EntityRenderer<SwordAura, EntityRenderState> {
    public SwordAuraRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    public void render(EntityRenderState renderState, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.SWORD_AURA));
        NarakaRenderUtils.renderFlatImage(poseStack, vertexConsumer, List.of(), packedLight, OverlayTexture.NO_OVERLAY, -1, Direction.EAST);
        super.render(renderState, poseStack, bufferSource, packedLight);
    }
}
