package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.color.block.BlockColors;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.Sheets;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

import java.util.List;

@Environment(EnvType.CLIENT)
public class BlockTransparentRenderer implements ResourceManagerReloadListener {
    public final static BlockTransparentRenderer INSTANCE = new BlockTransparentRenderer();

    private final Minecraft minecraft;
    @Nullable
    private BlockRenderDispatcher blockRenderer;
    @Nullable
    private BlockColors blockColors;

    public BlockTransparentRenderer() {
        this.minecraft = Minecraft.getInstance();
    }

    public void renderModel(
            PoseStack.Pose pose,
            VertexConsumer consumer,
            @Nullable BlockState state,
            BakedModel model,
            float red,
            float green,
            float blue,
            float alpha,
            int packedLight,
            int packedOverlay
    ) {
        RandomSource randomSource = RandomSource.create();
        for (Direction direction : Direction.values()) {
            randomSource.setSeed(42L);
            renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, direction, randomSource), packedLight, packedOverlay);
        }

        randomSource.setSeed(42L);
        renderQuadList(pose, consumer, red, green, blue, alpha, model.getQuads(state, null, randomSource), packedLight, packedOverlay);
    }

    private static void renderQuadList(
            PoseStack.Pose pose, VertexConsumer consumer, float red, float green, float blue, float alpha, List<BakedQuad> quads, int packedLight, int packedOverlay
    ) {
        for (BakedQuad bakedQuad : quads) {
            float tintedRed = 1;
            float tintedGreen = 1;
            float tintedBlue = 1;
            if (bakedQuad.isTinted()) {
                tintedRed = Mth.clamp(red, 0.0F, 1.0F);
                tintedGreen = Mth.clamp(green, 0.0F, 1.0F);
                tintedBlue = Mth.clamp(blue, 0.0F, 1.0F);
            }

            consumer.putBulkData(pose, bakedQuad, tintedRed, tintedGreen, tintedBlue, alpha, packedLight, packedOverlay);
        }
    }

    private boolean isRendererLoaded() {
        return blockRenderer != null && blockColors != null;
    }

    public void renderTransparentBlock(BlockState state, PoseStack poseStack, MultiBufferSource bufferSource, float alpha, int packedLight, int packedOverlay) {
        if (isRendererLoaded() && state.getRenderShape() == RenderShape.MODEL) {
            BakedModel bakedModel = blockRenderer.getBlockModel(state);
            RenderType renderType = Minecraft.useShaderTransparency() ? Sheets.translucentItemSheet() : Sheets.translucentCullBlockSheet();
            int color = this.blockColors.getColor(state, null, null, 0);
            float red = (float) (color >> 16 & 0xFF) / 255.0F;
            float green = (float) (color >> 8 & 0xFF) / 255.0F;
            float blue = (float) (color & 0xFF) / 255.0F;
            renderModel(
                    poseStack.last(),
                    bufferSource.getBuffer(renderType),
                    state,
                    bakedModel,
                    red, green, blue, alpha,
                    packedLight, packedOverlay
            );
        }
    }

    @Override
    public void onResourceManagerReload(ResourceManager resourceManager) {
        this.blockRenderer = minecraft.getBlockRenderer();
        this.blockColors = minecraft.getBlockColors();
    }
}
