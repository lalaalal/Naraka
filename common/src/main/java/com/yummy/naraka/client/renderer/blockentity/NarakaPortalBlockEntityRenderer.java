package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.block.entity.NarakaPortalBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;

@Environment(EnvType.CLIENT)
public class NarakaPortalBlockEntityRenderer implements BlockEntityRenderer<NarakaPortalBlockEntity> {
    private static final ResourceLocation[] TEXTURE_MAPPING = {
            NarakaTextures.NARAKA_PORTAL_1, NarakaTextures.NARAKA_PORTAL_1, NarakaTextures.NARAKA_PORTAL_2, NarakaTextures.NARAKA_PORTAL_3,
    };

    public NarakaPortalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    public ResourceLocation getTextureLocation(NarakaPortalBlockEntity blockEntity) {
        return TEXTURE_MAPPING[blockEntity.getUsage()];
    }

    public RenderType getRenderType(NarakaPortalBlockEntity blockEntity) {
        if (NarakaClientContext.SHADER_ENABLED.getValue())
            return RenderType.entityTranslucent(getTextureLocation(blockEntity));
        return NarakaRenderTypes.longinusCutout(getTextureLocation(blockEntity));
    }

    @Override
    public void render(NarakaPortalBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.scale(3, 3, 3);
        RenderType renderType = getRenderType(blockEntity);
        VertexConsumer vertexConsumer = bufferSource.getBuffer(renderType);
        NarakaRenderUtils.renderFlatImage(poseStack.last(), vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xbbffffff, Direction.Axis.X, false);
        if (!NarakaClientContext.SHADER_ENABLED.getValue())
            NarakaRenderUtils.renderFlatImage(poseStack.last(), vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xbbffffff, Direction.Axis.X, true);
        poseStack.popPose();
    }
}
