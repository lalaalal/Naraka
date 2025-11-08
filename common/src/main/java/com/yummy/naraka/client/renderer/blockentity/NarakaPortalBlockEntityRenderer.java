package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.client.NarakaClientContext;
import com.yummy.naraka.client.NarakaRenderTypes;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.client.renderer.blockentity.state.NarakaPortalRenderState;
import com.yummy.naraka.client.util.NarakaRenderUtils;
import com.yummy.naraka.world.block.entity.NarakaPortalBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

@Environment(EnvType.CLIENT)
public class NarakaPortalBlockEntityRenderer implements BlockEntityRenderer<NarakaPortalBlockEntity, NarakaPortalRenderState> {
    private static final ResourceLocation[] TEXTURE_MAPPING = {
            NarakaTextures.NARAKA_PORTAL_1, NarakaTextures.NARAKA_PORTAL_1, NarakaTextures.NARAKA_PORTAL_2, NarakaTextures.NARAKA_PORTAL_3,
    };

    public NarakaPortalBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public NarakaPortalRenderState createRenderState() {
        return new NarakaPortalRenderState();
    }

    @Override
    public void extractRenderState(NarakaPortalBlockEntity blockEntity, NarakaPortalRenderState renderState, float partialTick, Vec3 cameraPosition, @Nullable ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        BlockEntityRenderer.super.extractRenderState(blockEntity, renderState, partialTick, cameraPosition, breakProgress);
        renderState.usage = blockEntity.getUsage();
    }

    public ResourceLocation getTextureLocation(NarakaPortalRenderState renderState) {
        return TEXTURE_MAPPING[renderState.usage];
    }

    public RenderType getRenderType(NarakaPortalRenderState renderState) {
        if (NarakaClientContext.SHADER_ENABLED.getValue())
            return RenderType.entityTranslucent(getTextureLocation(renderState));
        return NarakaRenderTypes.longinusCutout(getTextureLocation(renderState));
    }

    @Override
    public void submit(NarakaPortalRenderState renderState, PoseStack poseStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        poseStack.pushPose();
        poseStack.translate(0.5, 0, 0.5);
        poseStack.scale(3, 3, 3);
        RenderType renderType = getRenderType(renderState);
        nodeCollector.submitCustomGeometry(poseStack, renderType, (pose, vertexConsumer) -> {
            NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, 0xbbffffff, Direction.Axis.X, false);
            if (!NarakaClientContext.SHADER_ENABLED.getValue())
                NarakaRenderUtils.renderFlatImage(pose, vertexConsumer, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY, -1, Direction.Axis.X, true);
        });
        poseStack.popPose();
    }
}
