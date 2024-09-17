package com.yummy.naraka.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.yummy.naraka.client.NarakaModelLayers;
import com.yummy.naraka.client.NarakaTextures;
import com.yummy.naraka.world.block.entity.SoulStabilizerBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;

@Environment(EnvType.CLIENT)
public class SoulStabilizerBlockEntityRenderer implements BlockEntityRenderer<SoulStabilizerBlockEntity> {
    private final ModelPart root;

    public SoulStabilizerBlockEntityRenderer(BlockEntityRendererProvider.Context context) {
        root = context.bakeLayer(NarakaModelLayers.FORGING_BLOCK);
    }

    @Override
    public void render(SoulStabilizerBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        poseStack.pushPose();
        VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(NarakaTextures.FORGING_BLOCK));
        root.render(poseStack, vertexConsumer, packedLight, packedOverlay);
        poseStack.popPose();
    }
}
