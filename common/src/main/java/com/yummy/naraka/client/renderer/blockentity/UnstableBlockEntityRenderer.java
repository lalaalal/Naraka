package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.world.block.entity.UnstableBlockEntity;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public class UnstableBlockEntityRenderer implements BlockEntityRenderer<UnstableBlockEntity> {
    private final BlockRenderDispatcher blockRenderDispatcher;

    public UnstableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        this.blockRenderDispatcher = context.getBlockRenderDispatcher();
    }

    @Override
    public void render(UnstableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        Level level = blockEntity.getLevel();
        if (level == null)
            return;
        BlockState state = blockEntity.getOriginal();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(ItemBlockRenderTypes.getChunkRenderType(state));
        blockRenderDispatcher.renderBatched(blockEntity.getOriginal(), blockEntity.getBlockPos(), level, poseStack, vertexConsumer, false, level.random);
    }
}
