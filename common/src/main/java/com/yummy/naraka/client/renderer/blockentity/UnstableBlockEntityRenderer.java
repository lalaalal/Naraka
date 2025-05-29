package com.yummy.naraka.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.yummy.naraka.world.block.entity.UnstableBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.Vec3;

@Environment(EnvType.CLIENT)
public class UnstableBlockEntityRenderer implements BlockEntityRenderer<UnstableBlockEntity> {
    public UnstableBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
    }

    @Override
    public void render(UnstableBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, Vec3 cameraPos) {

    }
}
