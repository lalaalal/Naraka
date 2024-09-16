package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

@Environment(EnvType.CLIENT)
public class SoulStabilizerBlockEntityRenderer implements BlockEntityRenderer<SoulStabilizerBlockEntity> {
    public SoulStabilizerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(SoulStabilizerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {

    }
}
